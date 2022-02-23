from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db
from datetime import date
import json

cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://premnews-99ac4-default-rtdb.europe-west1.firebasedatabase.app/'
})
ref = db.reference('/news')
# ref.delete() # remove this when done testing

url = 'https://www.independent.ie/sport/soccer/premier-league'
content = requests.get(url)
soup = BeautifulSoup(content.text, 'html.parser')
body = soup.find_all('div', class_='c-card1-main')

title_list = []
link_list = []
date_list = []

for item in body:
    title = item.get_text()
    title = title.strip().split('\n')[0]
    title_list.append(title)

    link = item.select_one('div.c-card1-main > a[href]')['href']
    link_list.append(link)

i=0
while i < len(title_list):
    print(title_list[i] + link_list[i] + '\n')
    i += 1
