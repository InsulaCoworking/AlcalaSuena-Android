import json

with open('data.json', 'r') as f:
	data_str = f.read()

data = json.loads(data_str)
for venue in data:
	for event in venue['events']:
		event['bands'] = [event['bands'][0]]


with open('data2.json', 'w') as f:
	f.write(json.dumps(data))

