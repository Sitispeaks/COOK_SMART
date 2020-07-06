from flask import Flask,request, jsonify
import pymongo
import re
from bson.objectid import ObjectId

app = Flask(__name__)

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["cook_smart"]
mycol = mydb["all_indian_recipe"]

def get_recipe_list(ingredinet_str):
    # ingredinet_str = "chicken,onion,garlic"
    query_ingredients = []

    for each_ingredient in ingredinet_str.split(","):
        each_query = {}
        each_query['recipe_ingredient.ingr'] = re.compile(r"{}".format(each_ingredient))
        query_ingredients.append(each_query)

    filter_ing= {'$and': query_ingredients}
    # filter_ing= {'$and': query_ingredients , '_id': False}
    result = mycol.find(filter=filter_ing) # , {'_id': False}


    result_receipes_arr = []

    for each_response_ele in result:
        each_response_ele['_id'] = str(each_response_ele['_id'])
        result_receipes_arr.append(each_response_ele)

    return result_receipes_arr

@app.route("/")
def main():
    return "<h1> Flask Working! </h1>"

@app.route("/recipe_by_ingrs", methods=['POST'])
def get_recipes():
    content = request.json
    ingredients_list_str = content['ingredients'] 
    all_recive_data = "{}".format(get_recipe_list(ingredients_list_str))
    return "{}".format(all_recive_data)


@app.route("/recipe_by_id", methods=['POST'])
def get_recipe_by_id():
    content = request.json
    result =  mycol.find({"_id": ObjectId(str(content['recipe_id']))})
    result_arr = []
    for obj in result:
        result_arr.append(obj)
    all_obj_data = "{}".format(result_arr)
    return "{}".format(all_obj_data)


if __name__ == "__main__":
    app.run()
