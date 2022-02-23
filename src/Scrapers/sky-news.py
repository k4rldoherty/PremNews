from unittest import result
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

url = 'https://news.sky.com/topic/premier-league-3810'
content = requests.get(url)
soup = BeautifulSoup(content.text, "html.parser")
body = soup.find_all("h3", "sdc-site-tile__headline")

titles_list = []
links_list = []
for item in body:
    headline = item.find_all('span', class_='sdc-site-tile__headline-text')[0].text
    titles_list.append(headline)

    link = item.find('a', class_='sdc-site-tile__headline-link').get('href')
    links_list.append('https://news.sky.com'  + link)

i = 0
while i < len(titles_list):
    if len(ref.order_by_child("Title").equal_to(titles_list[i]).get()) == 0:
        print("Dosent Exist")
        ref.push({
        'Title' : titles_list[i],
        'Link' : links_list[i] 
    })
    else:
        print("Already exists")
    i+=1
