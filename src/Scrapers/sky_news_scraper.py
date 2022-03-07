from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db
from datetime import date

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
    print(headline + "has been added successfully.")

    link = item.find('a', class_='sdc-site-tile__headline-link').get('href')
    links_list.append('https://news.sky.com'  + link)
    print(link + " has been added successfully.")

i = 0
print("Adding new stories to database...")
while i < len(titles_list):
    if "&" not in str(titles_list[i]):
        pass
    else:
        titles_list[i] = str(titles_list[i]).replace("&", " and ")
    
    if len(ref.order_by_child("Title").equal_to(titles_list[i]).get()) == 0:
        print("Dosent Exist. Adding ...")
        ref.push({
        'Title' : titles_list[i],
        'Link' : links_list[i],
        'Date Added' : str(date.today()),
        'Source' : 'Sky News' 
    })
    else:
        print("Already exists. Ignoring ...")
    i+=1

print("All news stories from Sky News have been added successfully\n\n")