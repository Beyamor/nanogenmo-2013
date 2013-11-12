from bs4 import BeautifulSoup
import urllib2
import re

soup	= BeautifulSoup(urllib2.urlopen("http://en.wikipedia.org/wiki/List_of_novelists"))
content	= soup.find(id="mw-content-text")

links = []
for author_list in content.find_all("ul", recursive=False):
	for author in author_list.find_all("li"):
		link = author.find("a", href=re.compile("/wiki.*"))

		if link:
			links.append(link.get("href"))

with open("authors", "w") as authors:
	for link in links:
		authors.write(link + "\n")
