from bs4 import BeautifulSoup 
import requests
import time
import recipe_url_fetcher as recipe_url
import new_ui_fetcher, old_ui_fetcher
import os
import json
import pymongo
from tqdm import tqdm
import multiprocessing.dummy as mp
import multiprocessing as mpcount

recipe_url_obj = recipe_url.RecipeUrlFetcher()
oldUIObj = old_ui_fetcher.OldUi()
newUIObj = new_ui_fetcher.NewUi()
all_recipe_obj_arr = []

# Mongo Initilizer
# Sample call
# { $and:[ {"recipe_ingredient.ingr": /chicken/}, {"recipe_ingredient.ingr": /onion/}, {"recipe_ingredient.ingr": /butter/} ] }

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["cook_smart"]
mycol = mydb["all_indian_recipe"]

file_name = 'all_recipe.json'
if os.path.exists(file_name):
    print("File found")
    with open(file_name) as json_file:  
        data = json.load(json_file)
        recipe_url_list = [obj['url'] for obj in data]
        # print(recipe_url_list)
else:
    # recipe_url # will take time please be patient
    print("File not found")
    recipe_url_list = recipe_url_obj.get_all_recipe_link()

# check ui path looking for adjust button (cause this is the only way to differenciate two type of ui)
path_to_adjust = '.recipe-adjust-servings'

def url_manage(url_to_fetch, count):
    print("{} Working for url: {}".format(count, url_to_fetch))
    res = requests.get(url_to_fetch)
    soup = BeautifulSoup(res.text, features="lxml")
    adjust_data_thr = soup.select(path_to_adjust)
    recipe_obj = {}
    if 'recipe-adjust-servings' in str(adjust_data_thr) :
        recipe_obj = newUIObj.recipe_obj(url_to_fetch)
        # all_recipe_obj_arr.append(new_ui_recipe_obj)
        # x = mycol.insert_one(new_ui_recipe_obj)
    else:
        recipe_obj = oldUIObj.recipe_obj(url_to_fetch)
        # all_recipe_obj_arr.append(old_ui_recipe_obj)
        
    # Adding into Mongo chnage the variable mycola to mycol
    x = mycola.insert_one(recipe_obj)
    # print(x)

    return recipe_obj


def main_recipe_extract_and_save_to_mongo():
    startTime = time.time()
    pool = mp.Pool(mpcount.cpu_count() -1 )

    for i in range(len(recipe_url_list)):
        # url_manage(recipe_url_list[i])
        result = pool.apply_async(url_manage, args=(recipe_url_list[i], i)).get()
    pool.close()
    pool.join()

    print("Done in {} secs.".format(time.time()-startTime))

main_recipe_extract_and_save_to_mongo()

    