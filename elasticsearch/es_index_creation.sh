#!/bin/bash

curl -XPUT "http://$1/library/" -d "{
	\"settings\": {
		\"analysis\": {
			\"analyzer\": {
				\"completion_analyzer\": {
					\"type\": \"custom\",
					\"tokenizer\": \"edgeNGram_tokenizer\",
					\"filter\": [
						\"lowercase\"
					]
				},
				\"search_analyzer\": {
					\"type\": \"custom\",
					\"tokenizer\": \"lowercase\"
				}
			},
			\"tokenizer\": {
				\"edgeNGram_tokenizer\": {
					\"type\": \"edgeNGram\",
					\"min_gram\": 1,
					\"max_gram\": 20,
					\"token_chars\": [
						\"letter\",
						\"digit\"
					]
				}
			}
		}
	},
	\"mappings\": {
		\"movie\": {
			\"properties\": {
				\"id\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"title\": {
					\"type\": \"string\",
					\"index_analyzer\": \"completion_analyzer\",
					\"search_analyzer\": \"search_analyzer\"
				},
				\"genres\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"mark\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"nb\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				}
			}
		},
		\"rating\": {
			\"properties\": {
    			\"user\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"movie\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"mark\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				}
			}
		},
		\"recommendation\": {
			\"properties\": {
    			\"user\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"movie\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				},
				\"mark\": {
					\"type\": \"string\",
					\"index\": \"not_analyzed\"
				}
			}
		}
	}
}"

exit 0

