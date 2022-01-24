from bs4 import BeautifulSoup
import requests
import re


def main():

    url = 'https://www.skysports.com/premier-league-news'
    content = requests.get(url)
    soup = BeautifulSoup(content.text, "html.parser")
    body = soup.find_all("div", "news-list__body")

    title_list = []
    article_list = []
    titles_plus_links = {}

    for item in body:
        title = item.find_all('h4', class_="news-list__headline")[0].text
        title_list.append(title.strip())
        articles = [i['href'] for i in item.find_all('a', class_="news-list__headline-link")]
        article_list.append(articles)

    titles_plus_links = {title_list[i]: article_list[i] for i in range(len(article_list))}

    for key, value in titles_plus_links.items():
        print(key + ' : ' + str(value) + '\n\n')

if __name__ == '__main__':
    main()