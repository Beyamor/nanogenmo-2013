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

INTERESTING_SECTION_TERMS = {
		None,
		"life",
		"death",
		"biography"
}

def is_interesting_section(section):
	if section is None:
		return True

	canonical_section = section.lower()
	for term in INTERESTING_SECTION_TERMS:
		if term and term in canonical_section:
			return True
	return False

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
		interesting_sentences	= []
		current_section		= None
		author_name		= parse_author_name(link)

		for content_item in content.children:
			if content_item.name == "h2":
				section_heading = content_item.find(class_="mw-headline")

				if section_heading:
					current_section = section_heading.get_text()

			elif content_item.name == "p" and is_interesting_section(current_section):
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

						interesting_sentences.append({
							"content": content,
							"contains-author-name": contains_author_name
						})

		if len(interesting_sentences) is not 0:
			sentence	= choice(interesting_sentences)
			number_of_words	= sentence["content"].count(" ") + 1 # whatevs, close enough
			novel_length	= novel_length + number_of_words
			novel.append(sentence)

			# wikipedia doesn't have a rate limit, but hey, let's not push it
			time.sleep(0.1)

	except e:
		print e

if options.novel_name is not None:
	novel_name = options.novel_name
else:
	novel_name = "novel-" + datetime.datetime.now().strftime("%A-%d-%B-%Y-%I:%M%p")

continuing_sentence	= r"(He|She|His|Her).*"
is_first_sentence	= True
with open(novel_name, "w") as novel_file:
	for sentence in novel:
		content = sentence["content"]

		if not is_first_sentence:
			if sentence["contains-author-name"]:
				novel_file.write("\n\n")
			else:
				novel_file.write(" ")

		novel_file.write(content.encode("utf-8"))
		is_first_sentence = False
