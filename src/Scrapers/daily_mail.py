from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db
from datetime import date
import json

ref = db.reference('/news')

url = 'https://www.dailymail.co.uk/sport/premierleague/index.html'
content = requests.get(url)
soup = BeautifulSoup(content.text, 'html.parser')
body = soup.find_all('div', 'article article-small articletext-right')

title_list = []
article_list = []

for item in body:
    title = item.find_all('span', class_='social-headline')[0].text
    title_list.append(title.strip())
    print(title + "has been added successfully.")

    article_link = item.select_one('h2.linkro-darkred > a[href]')['href']
    article_list.append(article_link)
    print(article_link + " has been added successfully.")

i=0
print("Adding new stories to database...")
while i < len(title_list):
    if "&" not in str(title_list[i]):
        pass
    else:
        title_list[i] = str(title_list[i]).replace("&", " and ")
    
    if len(ref.order_by_child("Title").equal_to(title_list[i]).get()) == 0:
        print("Dosent Exist. Adding ...")
        ref.push({
            'Title' : title_list[i],
            'Link' : article_list[i],
            'Date Added' : str(date.today()),
            'Source' : 'Daily Mail'
        })
    else:
        print("Already Exists. Ignoring ...")
    i+=1

print("All news stories from Daily Mail have been added successfully\n\n")