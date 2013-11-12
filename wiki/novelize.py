from bs4 import BeautifulSoup
from random import choice
import urllib2
import re
import nltk

INTERESTING_SECTIONS_TERMS = {"life", "death", "biography"}

author_links = []
with open("authors", "r") as authors_file:
	for link in authors_file.read().split("\n"):
		author_links.append("http://en.wikipedia.org" + link)

#link		= choice(author_links)
link		= "http://en.wikipedia.org/wiki/Brian_Moore_(novelist)"
soup		= BeautifulSoup(urllib2.urlopen(link))
content		= soup.find(id="mw-content-text")

section_content	= {}
current_section	= None
for content_item in content.children:
	if content_item.name == "h2":
		section_heading = content_item.find(class_="mw-headline")

		if section_heading:
			current_section = section_heading.get_text()

	elif content_item.name == "p" and current_section:
		if current_section not in section_content:
			section_content[current_section] = []

		for sentence in nltk.tokenize.sent_tokenize(content_item.get_text()):
			section_content[current_section].append(sentence)

interesting_content = []
for section, content in section_content.items():
	canonical_section	= section.lower()
	is_interesting		= False
	for term in INTERESTING_SECTIONS_TERMS:
		if term in canonical_section:
			is_interesting = True
			break

	if is_interesting:
		for sentence in content:
			interesting_content.append(sentence)

print choice(interesting_content)
