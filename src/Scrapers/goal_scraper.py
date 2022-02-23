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
# ref.delete()

url = 'https://www.goal.com/en-ie/premier-league/2kwbbcootiqqgmrzs6o5inle5'
content = requests.get(url)
soup = BeautifulSoup(content.text, "html.parser")
body = soup.find_all("div", class_= "page-container-bg")

print(body)