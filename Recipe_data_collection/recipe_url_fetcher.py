from bs4 import BeautifulSoup 
import requests
import time
import json
import os
from tqdm import tqdm

class RecipeUrlFetcher:

    def __init__(self):
        super().__init__()
        self.BASE_URL = 'https://www.allrecipes.com/recipes/233/world-cuisine/asian/indian/?page='
        self.recipe_links_all = []

    def get_max_page_count(self):
        count = 1
        path_to_error = '.error-page__404'
        while True:
            url = self.BASE_URL+'{}'.format(count)
            soup = BeautifulSoup(requests.get(url).text, features="lxml")
            error = soup.select(path_to_error)
            if 'page__404' in str(error):
                break
            else:
                # print("page count "+str(count))
                count+=1
        # print("all page count "+str(count))
        return count

    def get_all_recipe_link(self, write_to_json=1):

        # Serching max count of data avalibility
        MAX_PAGE_COUNT = self.get_max_page_count()

        urlObjArr = []

        for url in tqdm([ self.BASE_URL+str(x) for x in range(1, MAX_PAGE_COUNT)]):
            res = requests.get(url)
            soup = BeautifulSoup(res.text, features="lxml")
            recipe_name = soup.select('.fixed-recipe-card__title-link')
            recipe_links = soup.select('.r a')

            for a in soup.find_all('a', href=True):
                link = a['href']
                if 'https://www.allrecipes.com/recipe/' in link and link not in self.recipe_links_all: 
                    # print ("Found the URL:", link)
                    urlObj = {}
                    urlObj['url'] = link
                    self.recipe_links_all.append(link)
                    urlObjArr.append(urlObj)

        if write_to_json == 1:
            # for link in self.recipe_links_all :
            self.write_to_json('all_recipe.json', urlObjArr )

        return self.recipe_links_all

    def write_to_json(self, file_name, data):

        # file_path = os.getcwd()
        # f_loc = file_path+file_name

        # json_file = open(file_name, 'w+')


        # if not os.path.exists(f_loc):
        #     json_file = open(f_loc, 'w+').close()


        with open(file_name, 'w+', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=4)
