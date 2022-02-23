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

url = 'https://www.skysports.com/premier-league-news'
content = requests.get(url)
soup = BeautifulSoup(content.text, "html.parser")
body = soup.find_all("div", "news-list__body")

title_list = []
article_list = []
for item in body:
    title = item.find_all('h4', class_="news-list__headline")[0].text
    title_list.append(title.strip())
    
    articles = [i['href'] for i in item.find_all('a', class_="news-list__headline-link")]
    article_list.append(articles)


article_list = [item for sublist in article_list for item in sublist] # Changes the list of lists into a flat list

i=0
while i < len(title_list):
    ref.push({
        'Title' : title_list[i],
        'Link' : article_list[i] 
    })
    i+=1