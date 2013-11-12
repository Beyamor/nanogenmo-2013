from bs4 import BeautifulSoup
from random import choice
import urllib2
import re
import nltk
import time
import datetime

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

author_links = []
with open("authors", "r") as authors_file:
	for link in authors_file.read().split("\n"):
		author_links.append("http://en.wikipedia.org" + link)

novel		= []
novel_length	= 0
while novel_length < 500:
	link			= choice(author_links)
	soup			= BeautifulSoup(urllib2.urlopen(link))
	content			= soup.find(id="mw-content-text")
	section_sentences	= {}
	current_section		= None
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
			for sentence in sentences:
				sentence_without_citations	= re.sub("\[\d+\]", "", sentence)
				sentence_without_parens		= re.sub("\(.*?\)", "", sentence_without_citations)
				stripped_sentence		= sentence_without_parens.strip()

				if len(stripped_sentence) > 0:
					exiting_sentences.append(stripped_sentence)

	interesting_content = []
	for section, sentences in section_sentences.items():
		orderer	= section_sentence_orderer(section)
		index	= 1.0

		for sentence in sentences:
			position	= index / len(sentences)
			order		= orderer(section=section, position=position)
			index		= index + 1
			interesting_content.append((sentence, order))


	if len(interesting_content) is not 0:
		(sentence, order)	= choice(interesting_content)
		novel_length		= novel_length + len(sentence)
		novel.append((sentence, order))

		# wikipedia doesn't have a rate limit, but whatever, let's do 'em a solid
		time.sleep(0.1)

ordered_novel	= sorted(novel, key=lambda whatever: whatever[1])
novel_name	= "novel-" + datetime.datetime.now().strftime("%A-%d-%B-%Y-%I:%M%p")
with open(novel_name, "w") as novel:
	for (sentence, _) in ordered_novel:
		novel.write((sentence + "\n").encode("utf8"))
