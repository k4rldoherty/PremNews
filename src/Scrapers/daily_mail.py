from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://premnews-99ac4-default-rtdb.europe-west1.firebasedatabase.app/'
})
ref = db.reference('/news')
ref.delete()

url = 'https://www.dailymail.co.uk/sport/premierleague/index.html'
content = requests.get(url)
soup = BeautifulSoup(content.text, 'html.parser')
body = soup.find_all('div', 'article article-small articletext-right')
title_list = []
article_list = []

for item in body:
    title = item.find_all('span', class_='social-headline')[0].text
    title_list.append(title.strip())

    article_link = item.select_one('h2.linkro-darkred > a[href]')['href']
    article_list.append(article_link)

i=0
while i < len(title_list):
    ref.push({
        'Title' : title_list[i],
        'Link' : article_list[i] 
    })
    i+=1