#!/usr/bin/env python3
import argparse
import requests
import sys
import logging as log

parser = argparse.ArgumentParser(description='Deploy app to Amazon Appstore')
parser.add_argument('--run', required=False, action='store_true', help='Run the script')
# to be mandatory script arguments
parser.add_argument('--client_id', required=True, help='client_id - you get this from amazon portal, apps&services ribbon, API access, Security profiles, Web Settings, looks like amzn1.application-oa2-client.XXX...')
parser.add_argument('--client_secret', required=True, help='client_secret - you get this from amazon portal, apps&services ribbon, API access, Security profiles, Web Settings, this is a 64 char string')
parser.add_argument('--app_id', required=True, help='app_id - you get this from amazon portal, apps&services ribbon, My app, App, App Submission API Keys, amzn1.devportal.mobileapp.XXX...')
parser.add_argument('--local_apk_path', required=True, help='apk path on local computer. Example: d:/myapp.apk')
parser.add_argument('--verbose', required=True, action='store_true', help='verbose output')


args = parser.parse_args()
arg_client_id = args.client_id
arg_client_secret = args.client_secret
arg_app_id = args.app_id
arg_local_apk_path = args.local_apk_path
arg_recent_changes = "test"

if args.verbose:
    log.basicConfig(level=log.INFO)
else:
    log.basicConfig(level=log.WARNING)

if args.run:
    log.info("Running script")
    
else:
    log.info("use --run to run script, displaying current arguments")
    print(vars(args))
    sys.exit(0)

#@@@@@@@@@@ Obtain Access Token
#  Values that you need to provide
log.info("assigning variables")
BASE_URL = 'https://developer.amazon.com/api/appstore'
client_id = arg_client_id
client_secret = arg_client_secret
app_id = arg_app_id
local_apk_path = arg_local_apk_path

log.info("obtaining access token")
scope = "appstore::apps:readwrite"
grant_type = "client_credentials"
data = {
    "grant_type": grant_type,
    "client_id": client_id,
    "client_secret": client_secret,
    "scope": scope
}
amazon_auth_url = "https://api.amazon.com/auth/o2/token"
auth_response = requests.post(amazon_auth_url, data=data)

# Read token from auth response
auth_response_json = auth_response.json()
auth_token = auth_response_json["access_token"]

auth_token_header_value = "Bearer %s" % auth_token

headers = {
    "Authorization": auth_token_header_value,
}

if auth_response.status_code != 200:
    log.error("Error obtaining access token??")
    log.error(auth_response.json())

#@@@@@@@@@@ Create a New Edit
log.info("creating new edit")
# try/except 
try:
    create_edit_path = '/v1/applications/%s/edits' % app_id
    create_edit_url = BASE_URL + create_edit_path
    create_edit_response = requests.post(create_edit_url, headers=headers)
    edit_id = create_edit_response.json()['id']
except:
    log.error("Error creating new edit")
    log.error(create_edit_response.json())
    log.error("breaking error, or edit already exists")

#@@@@@@@@@@ Get the Open Edit
log.info("getting open edit")
get_edits_path = '/v1/applications/%s/edits' % app_id
get_edits_url = BASE_URL + get_edits_path
get_edits_response = requests.get(get_edits_url, headers=headers)
current_edit = get_edits_response.json()
edit_id = current_edit['id']
etag = get_edits_response.headers.get('ETag')
log.info("edit_id: %s" % edit_id)
log.info("etag: %s" % etag)

#@@@@@@@@@@ REPLACE APK
log.info("replacing apk")
## Get the current list of APKs
get_apks_path = '/v1/applications/%s/edits/%s/apks' % (app_id, edit_id)
get_apks_url = BASE_URL + get_apks_path
apks = requests.get(get_apks_url, headers=headers)

firstAPK = apks.json()[0]
apk_id = firstAPK['id']
replace_apk_path = '/v1/applications/%s/edits/%s/apks/%s/replace' % (app_id, edit_id, apk_id)

apkquery = get_edits_url + "/%s" % edit_id + "/apks/%s" % apk_id
apkquery_response = requests.get(apkquery, headers=headers)
apk_etag = apkquery_response.headers.get('ETag')


## Open the apk file on your local machine
try:
    log.info("Reading local apk file")
    local_apk = open(local_apk_path, 'rb').read()
except:
    log.error("Error opening local apk file")
    sys.exit(1)

replace_apk_url = BASE_URL + replace_apk_path
all_headers = {
    'Content-Type': 'application/vnd.android.package-archive',
    'If-Match': apk_etag
}
all_headers.update(headers)
replace_apk_response = requests.put(replace_apk_url, headers=all_headers, data=local_apk)

log.info(replace_apk_response.json())
#@@@@@@@@@@@@@ update listing

# getlisting
get_listing_path = '/v1/applications/%s/edits/%s/listings' % (app_id, edit_id)
get_listing_url = BASE_URL + get_listing_path + "/en-US"
get_listing_response = requests.get(get_listing_url, headers=headers)
listing_etag = get_listing_response.headers.get('ETag')
log.info(get_listing_response.json())
log.info(etag)
listing = get_listing_response.json()
# setlisting
listing['recentChanges'] = arg_recent_changes
set_listing_path = '/v1/applications/%s/edits/%s/listings' % (app_id, edit_id)
set_listing_url = BASE_URL + set_listing_path + "/en-US"
all_headers = {
    'If-Match': listing_etag
}
all_headers.update(headers)
set_listing_response = requests.put(set_listing_url, headers=all_headers, json=listing)
log.info(set_listing_response.json())

#@@@@@@@@@@@@@ commit
# query the current edit and etags before commit

log.info("getting open edit")
get_edits_path = '/v1/applications/%s/edits' % app_id
get_edits_url = BASE_URL + get_edits_path
get_edits_response = requests.get(get_edits_url, headers=headers)
current_edit = get_edits_response.json()
edit_id = current_edit['id']
etag = get_edits_response.headers.get('ETag')
log.info("edit_id: %s" % edit_id)
log.info("etag: %s" % etag)

query = get_edits_url + "/%s" % edit_id + "/commit" 
all_headers = {"if-match": etag}
all_headers.update(headers)
query_response = requests.post(query, headers=all_headers)
log.info(query_response.json())

