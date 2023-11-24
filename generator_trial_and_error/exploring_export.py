import json


def exploring_export():
    with open('resources/export.json', 'r') as f:
        data = json.load(f)

    for element in data:
        for key in element.keys():
            print(key)


if __name__ == '__main__':
    exploring_export()
