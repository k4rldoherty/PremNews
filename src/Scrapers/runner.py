from ast import Try
from logging import exception
from tkinter import E
import firebase_admin
from firebase import firebase
from firebase_admin import credentials
from firebase_admin import db

cred = credentials.Certificate('firebase-sdk.json')
firebase_admin.initialize_app(cred, {
    'databaseURL' : 'https://premnews-99ac4-default-rtdb.europe-west1.firebasedatabase.app/'
})

ref = db.reference('/news')
db_len = len(ref.order_by_child('Title').get())

try:

    import daily_mail
    import official_prem_scraper
    import independent_scraper
    import sky_news_scraper
    print('\nThe scrapers are currently working correctly. ' + '\n' +  'The database currently contains ' + str(db_len) + ' news arcticles from 4 sources')

except FileNotFoundError:
    print("There is a missing file!")