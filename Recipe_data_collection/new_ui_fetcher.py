from bs4 import BeautifulSoup 
import requests
import utils


class NewUi:

    def __init__(self):
        super().__init__()

    def get_data_from_xpath(self, soup, path, typecast_to_str = 0):
        recipe_data = soup.select(path)
        if recipe_data != None:
            if typecast_to_str == 0:
                data = [utils.cleanHeadings(title.text) for title in recipe_data if utils.cleanHeadings(title.text) != '']
                # if len(data) > 1:
                #     return data
                # else:
                #     return data[0]
                return data
            else:
                if len(recipe_data) != 0:
                    return recipe_data
        else:
            return []
    
    def get_image(self, soup, path):
        # srcs = [img['src'] for img in soup.select(path) if 'userphotos' in str(img['src'])]
        srcs = []
        for img in soup.select(path):
            if len(str(img)) != 0:
                if 'userphotos' in str(img):
                    srcs.append(img['src'])
            else:
                srcs.append("")

        output_urls = []
        for each_image_src in srcs:
            if 'https://images.media-allrecipes.com/' in each_image_src:
                if each_image_src.startswith('https://imagesvc.meredithcorp'):
                    clean_url = str(str(each_image_src.split("url=")[1]).split(".jpg")[0]) + '.jpg'
                    # print(clean_url)
                    output_urls.append(clean_url)
                else:
                    output_urls.append(each_image_src) 
        return output_urls

    def get_name(self, soup,path_recipe_name):
        name = self.get_data_from_xpath(soup,path_recipe_name)

        if len(name) == 0:
            return ''
        else:
            return name[-1]

    def serving_cleaner(self, soup, path_recipe_servings, pred_or_serve=0):
        data = self.get_data_from_xpath(soup,path_recipe_servings)

        if data != None or len(str(data)) == 0 or len(data) == 0:
            return ''
        else:
            if pred_or_serve == 0 :
                return data[0]
            else:
                return data[-1]

    def recipe_nutrition(self, soup, path_recipe_calories):
        data = self.get_data_from_xpath(soup,path_recipe_calories)

        if data != None or len(str(data)) == 0 or len(data) == 0:
            return ''
        else:
            return str(data).split(";")[1]
            
    def clean_cals(self, data):
        if len(str(data)) != 0:
            return int(utils.clean_calories(data))
        else:
            return 0


    def recipe_obj(self, url):
        res = requests.get(url)
        soup = BeautifulSoup(res.text, features="lxml")

        recipe_data_ = {}

        path_recipe_name = '.heading-content' 
        path_recipe_image = 'img'
        path_recipe_servings = '.recipe-meta-item-body' 
        path_recipe_calories = '.recipe-nutrition-section .section-body' 
        path_recipe_ingr = '.ingredients-item-name' 
        path_recipe_desc = '.margin-0-auto'
        path_recipe_procedure = '.section-body p'

        recipe_data_['category'] = 'Indian'
        recipe_data_['recipe_url'] = url
        recipe_data_['recipe_name'] = utils.cleanHeadings(str(self.get_name(soup,path_recipe_name)))
        recipe_data_['recipe_short_description'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup,path_recipe_desc)))
        recipe_data_['image'] = [{'url': x } for x in self.get_image(soup,path_recipe_image)]
        recipe_data_['recipe_calories'] = self.clean_cals(utils.cleanHeadings(str(self.get_data_from_xpath(soup,path_recipe_calories)).split(";")[0]))
        recipe_data_['recipe_preptime'] = utils.cleanHeadings(str(self.serving_cleaner(soup,path_recipe_servings, 0)))
        recipe_data_['recipe_servings'] = int(utils.cleanHeadings(str(self.serving_cleaner(soup,path_recipe_servings, 1)))) if len(utils.cleanHeadings(str(self.serving_cleaner(soup,path_recipe_servings, 1)))) != 0 else 0
        recipe_data_['recipe_ingredient'] = [{'data': x, 'amount': utils.cleanHeadings(utils.get_proper_name(x, 1)),'ingr': utils.cleanHeadings(utils.get_proper_name(x, 2)), 'content': utils.cleanHeadings(utils.get_proper_name(x))} for x in self.get_data_from_xpath(soup,path_recipe_ingr)]
        recipe_data_['recipe_description'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup,path_recipe_desc)))
        recipe_data_['recipe_procedure'] = [{'step': utils.cleanHeadings(str(stepcount)), 'desc': utils.cleanHeadings(desc)} for stepcount, desc in enumerate( self.get_data_from_xpath(soup,path_recipe_procedure))]
        recipe_data_['recipe_nutrition'] = utils.cleanHeadings(str(self.recipe_nutrition(soup,path_recipe_calories)))


        return recipe_data_



