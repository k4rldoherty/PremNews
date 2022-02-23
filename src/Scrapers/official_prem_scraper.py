from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db
from datetime import date

cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://premnews-99ac4-default-rtdb.europe-west1.firebasedatabase.app/'
})
ref = db.reference('/news')

url = 'https://www.premierleague.com/news'
content = requests.get(url)
soup = BeautifulSoup(content.text, "html.parser")
body = soup.find_all("ul", class_= "newsList contentListContainer")
titles = soup.find_all('span', class_='title')
links = soup.find_all('section', class_='featuredArticle')
title_list = []
link_list = []

for title in titles:
    title = title.get_text()
    if title.endswith("External Link"):
        title = title[:-13]
    title_list.append(title)

for link in links:
    link = link.select_one("div.col-9-m > a[href]")["href"]
    if link.startswith('https://www.premierleague.com') == False:
        link = 'https://www.premierleague.com' + link
    link_list.append(link)

i = 0
while i < len(title_list):
    if len(ref.order_by_child("Title").equal_to(title_list[i]).get()) == 0:
        print("Dosent Exist. Adding ...")
        ref.push({
            'Title' : title_list[i],
            'Link' : link_list[i],
            'Date Added' : str(date.today()),
            'Source' : 'Premier League Official Website'
        })
    else:
        print("Already Exists. Ignoring ...")
    i+=1
    
    