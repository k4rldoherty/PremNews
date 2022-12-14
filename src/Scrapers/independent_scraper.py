from bs4 import BeautifulSoup
import requests
import re
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db
from datetime import date
import json

# Function to change the link of an article to the title in cases where the Title has not been scraped correctly
def convert_link_2_title(string):
    string = string.split('/')
    string = string[-1].capitalize().split('-')[:-1]
    string = ' '.join(string)
    return string

ref = db.reference('/news')

url = 'https://www.independent.ie/sport/soccer/premier-league'
content = requests.get(url)
soup = BeautifulSoup(content.text, 'html.parser')
body = soup.find_all('div', class_='c-card1-main')

title_list = []
link_list = []

for item in body:
    title = item.get_text()
    title = title.strip().split('\n')[0]
    title_list.append(title)
    print(title + " has been added successfully.")

    link = item.select_one('div.c-card1-main > a[href]')['href']
    link_list.append(link)
    print(link + " has been added successfully.")

i=0
print("Adding new stories to database...")
while i < len(title_list):
    if len(str(title_list[i])) < 20:
        title_list[i] = str(convert_link_2_title(link_list[i]))
    if "&" not in str(title_list[i]):
        pass
    else:
        title_list[i] = str(title_list[i]).replace("&", " and ")

    if len(ref.order_by_child("Title").equal_to(title_list[i]).get()) == 0:
        print("Dosent Exist. Adding ...")
        ref.push({
            'Title' : title_list[i],
            'Link' : link_list[i],
            'Date Added' : str(date.today()),
            'Source' : 'Independent'
        })
    else:
        print("Already Exists. Ignoring ...")
    i+=1

print("All news stories from The Independent have been added successfully\n\n")