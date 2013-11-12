from bs4 import BeautifulSoup
from random import choice
import urllib2
import re
import nltk
import time
import datetime
import sys
import optparse

opts_parser = optparse.OptionParser()
opts_parser.add_option("-w", "--words", dest="desired_length", type="int", default=500)
opts_parser.add_option("-f", "--f", dest="novel_name")
(options, _) = opts_parser.parse_args()

def constantly(value):
	def f(**kwargs):
		return value
	return f

def positional_order(position=0, **kwargs):
	return position

def life_order(section="", **kwargs):
	if "early" in section:
		return 0

	else:
		return positional_order(**kwargs)

SECTION_SENTENCE_ORDERERS = {
		None:		positional_order,
		"life":		life_order,
		"death":	constantly(1),
		"biography":	positional_order
}

def is_interesting_section(section):
	if section is None:
		return True

	canonical_section = section.lower()
	for term in SECTION_SENTENCE_ORDERERS:
		if term and term in canonical_section:
			return True
	return False

def section_sentence_orderer(section):
	if section is None:
		return SECTION_SENTENCE_ORDERERS[None]
	else:
		canonical_section = section.lower()
		for (term, orderer) in SECTION_SENTENCE_ORDERERS.items():
			if term and term in canonical_section:
				return orderer

	raise Exception("No orderer for " + section)

def parse_author_name(link):
	name	= link[len("/wiki/"):]
	name	= urllib2.unquote(name).decode("utf8")
	name	= name.replace("_", " ")
	return name

author_links = []
with open("authors") as authors_file:
	for link in authors_file.read().split("\n"):
		author_links.append(link)

novel		= []
novel_length	= 0
while novel_length < options.desired_length:
	try:
		link			= choice(author_links)
		soup			= BeautifulSoup(urllib2.urlopen("http://en.wikipedia.org" + link).read().decode("utf-8"))
		content			= soup.find(id="mw-content-text")
		section_sentences	= {}
		current_section		= None
		author_name		= parse_author_name(link)

		for content_item in content.children:
			if content_item.name == "h2":
				section_heading = content_item.find(class_="mw-headline")

				if section_heading:
					current_section = section_heading.get_text()

			elif content_item.name == "p" and is_interesting_section(current_section):
				if not current_section in section_sentences:
					section_sentences[current_section] = []
				exiting_sentences = section_sentences[current_section]

				sentences = nltk.tokenize.sent_tokenize(content_item.get_text())
				for content in sentences:
					content	= re.sub(r"\[\d+\]", "", content)
					content	= re.sub(r"\(.*?\)", "", content)
					content	= re.sub(r"\[citation needed\]", "", content)
					content	= re.sub(r" +", " ", content)
					content	= re.sub(r" ,", ",", content)
					content	= re.sub(r" \.", ".", content)
					content	= re.sub(r",+", ",", content)
					content	= re.sub(r" +cit\.", "", content)
					content	= content.strip()

					if len(content) > 0:
						contains_author_name = (author_name in content)
						if not contains_author_name:
							for name in author_name.split(" "):
								if len(name) > 1 and name in content:
									contains_author_name = True
									break

						exiting_sentences.append({
							"content": content,
							"contains-author-name": contains_author_name
						})

		interesting_content = []
		for section, sentences in section_sentences.items():
			orderer	= section_sentence_orderer(section)
			index	= 1.0

			for sentence in sentences:
				position		= index / len(sentences)
				order			= orderer(section=section, position=position)
				index			= index + 1
				sentence["order"]	= order
				interesting_content.append(sentence)


		if len(interesting_content) is not 0:
			sentence	= choice(interesting_content)
			number_of_words	= sentence["content"].count(" ") + 1 # whatevs, close enough
			novel_length	= novel_length + number_of_words
			novel.append(sentence)

			# wikipedia doesn't have a rate limit, but hey, let's not push it
			time.sleep(0.1)

	except:
		pass

if options.novel_name is not None:
	novel_name = options.novel_name
else:
	novel_name = "novel-" + datetime.datetime.now().strftime("%A-%d-%B-%Y-%I:%M%p")

ordered_novel		= sorted(novel, key=lambda sentence: sentence["order"])
continuing_sentence	= r"(He|She|His|Her).*"
is_first_sentence	= True
with open(novel_name, "w") as novel:
	for sentence in ordered_novel:
		content = sentence["content"]

		if not is_first_sentence:
			if sentence["contains-author-name"]:
				novel.write("\n\n")
			else:
				novel.write(" ")

		novel.write(content.encode("utf-8"))
		is_first_sentence = False
