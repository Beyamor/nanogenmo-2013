from bs4 import BeautifulSoup
from random import choice
import urllib2
import re
import nltk

INTERESTING_SECTIONS_TERMS = {
	"life",
	"death",
	"biography"
}

def is_interesting_section(section):
	if not section:
		return True

	canonical_section = section.lower()
	for term in INTERESTING_SECTIONS_TERMS:
		if term in canonical_section:
			return True
	return False

author_links = []
with open("authors", "r") as authors_file:
	for link in authors_file.read().split("\n"):
		author_links.append("http://en.wikipedia.org" + link)

link		= choice(author_links)
soup		= BeautifulSoup(urllib2.urlopen(link))
content		= soup.find(id="mw-content-text")

section_content		= {}
current_section		= None
interesting_content	= []
for content_item in content.children:
	if content_item.name == "h2":
		section_heading = content_item.find(class_="mw-headline")

		if section_heading:
			current_section = section_heading.get_text()

	elif content_item.name == "p" and is_interesting_section(current_section):
		for sentence in nltk.tokenize.sent_tokenize(content_item.get_text()):
			sentence_without_citations = re.sub("\[\d+\]", "", sentence)
			interesting_content.append(sentence_without_citations)

if len(interesting_content) is not 0:
	print choice(interesting_content)
else:
	print "No interesting content for " + link
