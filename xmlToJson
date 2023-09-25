# 13 XML - JSON
# Среда

import os

import xmltodict
import json

from xml.dom import minidom
import simplejson as json

Path = os.path.dirname(os.path.abspath(__file__))


file = open(Path + "/shedule.xml", "r")
f_lines = file.readlines()
f_lines.pop(0)

# print(f_lines)
obj_classes = []


obj_list = []

for i in range(len(f_lines)):
    obj_list.append(f_lines[i].split('\n'))


index_to_clear = []

for i in range(len(obj_list)):
    if len(obj_list[i]) == 0:
        index_to_clear.append(i)


for i in range(len(index_to_clear)):
    obj_list.pop(index_to_clear[i])


for i in range(len(obj_list)):
    if obj_list[i][0].find("<") != -1:
        obj_classes.append([
            {"obj": obj_list[i]},
            {"type": "class"},
            {"index": i},
        ])
    else:
        obj_classes.append([
            {"obj": obj_list[i]},
            {"type": "text"},
            {"index": i},
        ])

# выдаем индексы

for i in range(len(obj_classes)):
    n = 0

    if (obj_classes[i][1]["type"] == "class"):
        for b in range(i+1, len(obj_classes)):

            if obj_classes[b][0]["obj"][0] == obj_classes[i][0]["obj"][0]:
                n += 1

            word = obj_classes[b][0]["obj"][0].replace("/", "")

            if obj_classes[i][0]["obj"][0] == word:
                obj_classes[b][2]["index"] = obj_classes[i][2]["index"]
                break


d = open(Path + '/shedule.json', 'w')  # открываем файл на запись


d.write('{\n')  # фигурные скобки для создания объекта

for i in range(len(obj_classes)):
    if (len(obj_classes[i][0]["obj"]) > 1):
        obj_classes[i][0]["obj"].pop(1)

for i in range(len(obj_classes)):
    if obj_classes[i][1]["type"] == "class":
        obj_classes[i][0]["obj"][0] = obj_classes[i][0]["obj"][0].replace(
            "<", "").replace(">", "")

    if obj_classes[i][1]["type"] == "text":
        obj_classes[i][0]["obj"][0] = obj_classes[i][0]["obj"][0].replace(
            " ", "")


# for i in range(len(obj_classes)):
#     print(obj_classes[i])

k = 0

# print("\n\n\n")

# != -1 найдено
# == -1 не найдено

for i in range(len(obj_classes)):
    if obj_classes[i][1]["type"] == "class":
        if obj_classes[i][0]["obj"][0].find("/") != -1:
            k = obj_classes[i][0]["obj"][0].count(" ")
            if obj_classes[i-1][1]["type"] != "text":
                if i != len(obj_classes)-1:
                    if obj_classes[i+1][0]["obj"][0].find("/") != -1:
                        d.write(' '*k + '}\n')
                    if obj_classes[i+1][0]["obj"][0].find("/") == -1:
                        d.write(' '*k + '},\n')
                else:
                    d.write('}\n')
        # print(obj_classes[i+1])
        if obj_classes[i][0]["obj"][0].find("/") == -1:
            k = obj_classes[i][0]["obj"][0].count(" ")
            if obj_classes[i+1][1]["type"] == "text":

                d.write(' '*k + '"' +
                        obj_classes[i][0]["obj"][0].replace(" ", "") + '" : ')
            else:
                d.write(
                    ' '*k + '"' + obj_classes[i][0]["obj"][0].replace(" ", "") + '" : {\n')
    else:
        if obj_classes[i+1][0]["obj"][0].find("/") != -1:
            if obj_classes[i+2][0]["obj"][0].find("/") == -1:
                d.write('"' + obj_classes[i][0]["obj"][0] + '",\n')
            else:
                d.write('"' + obj_classes[i][0]["obj"][0] + '"\n')


d.write('}\n')  # фигурные скобки для завершения объекта


# my_data = open(Path + '/shedule.xml', 'r')
# xpars = xmltodict.parse(my_data.read())
# json = json.dumps(xpars)


def parse_element(element):
    dict_data = dict()
    if element.nodeType == element.TEXT_NODE:
        dict_data['data'] = element.data
    if element.nodeType not in [element.TEXT_NODE, element.DOCUMENT_NODE,
                                element.DOCUMENT_TYPE_NODE]:
        for item in element.attributes.items():
            dict_data[item[0]] = item[1]
    if element.nodeType not in [element.TEXT_NODE, element.DOCUMENT_TYPE_NODE]:
        for child in element.childNodes:
            child_name, child_dict = parse_element(child)
            if child_name in dict_data:
                try:
                    dict_data[child_name].append(child_dict)
                except AttributeError:
                    dict_data[child_name] = [dict_data[child_name], child_dict]
            else:
                dict_data[child_name] = child_dict
    return element.nodeName, dict_data


dom = minidom.parse(Path + '/shedule.xml')
f = open(Path + '/shedule-lab.json', 'w')
f.write(json.dumps(parse_element(dom), sort_keys=True, indent=4))
f.close()
