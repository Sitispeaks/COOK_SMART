from bs4 import BeautifulSoup 
import requests
import utils


class OldUi:

    def __init__(self):
        super().__init__()

    def get_data_from_xpath(self, soup, path, typecast_to_str = 0):
        recipe_data = soup.select(path)
        if recipe_data != None or len(str(recipe_data)) == 0 :
            if typecast_to_str == 0:
                data = [utils.cleanHeadings(title.text) for title in recipe_data if len(str(utils.cleanHeadings(title.text))) != 0]
                # if len(data) > 2:
                #     return data
                # else:
                #     return data[0]
                return data
            else:
                if len(str(recipe_data)) != 0:
                    return recipe_data
        else:
            return []


    def select_image(self, soup, path_recipe_image):
        # data_is_thr = str(str(self.get_data_from_xpath(soup, path_recipe_image, 1)).split("src=")[1])[:-3] 
        data_is_thr = self.get_data_from_xpath(soup, path_recipe_image, 1) 
        data = ''
        if data_is_thr != None:
            if len(data_is_thr) != 0:
                data = str(str(data_is_thr).split("src=")[1])[:-3]
            else:
                data = ''
            return data
        else:
            return ''

        # return data
         
    def data_servings(self, soup, path_recipe_servings):
        servng_data = self.get_data_from_xpath(soup, path_recipe_servings, 1)

        if servng_data != None:
            if len(str(servng_data)) == 0:
                return ''
            elif 'content=' in str(servng_data):
                data_serv = utils.cleanHeadings(str(servng_data).split("content=")[1].split(" ")[0])
                data_serv = data_serv.replace("calories", "")
                data_serv = data_serv.replace("cals", "")
                return data_serv
            else:
                return ''
        else:
            return ''

    def clean_cals(self, data):
        if len(str(data)) != 0:
            return int(utils.clean_calories(data))
        else:
            return 0
        

    def recipe_obj(self, url):

        res = requests.get(url)
        soup = BeautifulSoup(res.text, features="lxml")

        recipe_data_ = {}

        path_recipe_name = '#recipe-main-content'
        path_recipe_image = '#BI_openPhotoModal1'
        path_recipe_servings = '.recipe-ingredients__header__toggles' #'#servings-button' #'.ng-binding' #
        path_recipe_calories = '#nutrition-button' 
        path_recipe_nutriton = '.nutrition-summary-facts'
        path_recipe_preptime = '.ready-in-time'
        path_recipe_ingr = '.checkList__line:nth-child(1) .added , .checkList__line:nth-child(2) .added , .checkList__line:nth-child(3) .added , .checkList__line:nth-child(4) .added , .checkList__line:nth-child(5) .added , .checkList__line:nth-child(6) .added' #'.added'
        path_recipe_desc = '.submitter__description'
        path_recipe_procedure = '.recipe-directions__list--item'

        # print(str(str(self.get_data_from_xpath(soup, path_recipe_image, 1)).split("src=")[1])[:-3])
        # print(self.get_data_from_xpath(soup, path_recipe_servings, 1))
        # print("S  {}".format(self.select_image(soup, path_recipe_image)))
        # print(self.get_data_from_xpath(soup, path_recipe_ingr))

        recipe_data_['category'] = 'Indian'
        recipe_data_['recipe_url'] = url
        recipe_data_['recipe_name'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_name)))
        recipe_data_['recipe_short_description'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_desc)))
        recipe_data_['image'] = [{'url': self.select_image(soup, path_recipe_image) }]
        recipe_data_['recipe_calories'] = self.clean_cals(utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_calories))))
        recipe_data_['recipe_preptime'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_preptime)))
        recipe_data_['recipe_servings'] = int(utils.cleanHeadings(str(self.data_servings(soup, path_recipe_servings)))) if len(utils.cleanHeadings(str(self.data_servings(soup, path_recipe_servings)))) != 0 else 0 
        recipe_data_['recipe_ingredient'] = [{'ingredient_desc': x, 'amount': utils.get_proper_name(x, 1), 'ingr': utils.cleanHeadings(utils.get_proper_name(x, 2)), 'content': utils.cleanHeadings(utils.get_proper_name(x))} for x in self.get_data_from_xpath(soup, path_recipe_ingr)]
        recipe_data_['recipe_description'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_desc)))
        recipe_data_['recipe_procedure'] = [{'step': utils.cleanHeadings(str(stepcount)), 'desc': utils.cleanHeadings(desc)} for stepcount, desc in enumerate( self.get_data_from_xpath(soup, path_recipe_procedure))]
        recipe_data_['recipe_nutrition'] = utils.cleanHeadings(str(self.get_data_from_xpath(soup, path_recipe_nutriton)))

        return recipe_data_


