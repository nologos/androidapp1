#!/usr/bin/env python3
import argparse
import requests
import sys
import logging as log



def authenticate(client_id, client_secret):
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
    try:
        # Read token from auth response
        auth_response_json = auth_response.json()
        auth_token = auth_response_json["access_token"]

        auth_token_header_value = "Bearer %s" % auth_token

        headers = {
            "Authorization": auth_token_header_value,
        }
        return headers
    except:
        log.error("Error obtaining access token??")
        log.error(auth_response.json())
        return 0

def editFull():
    edits_url = BASE_URL + '/v1/applications/%s/edits' % app_id
    try:
        log.info("getting edit info")
        create_edit_response = requests.get(edits_url, headers=headers)
        edit_id = create_edit_response.json()['id']
    except:
        log.info("no edit found, creating new edit")
        create_edit_response = requests.post(edits_url, headers=headers)
        edit_id = create_edit_response.json()['id']
    etag = create_edit_response.headers.get('ETag')
    log.info("edit info: " +  edit_id +" "+ etag)
    return edit_id, etag

def APKreplace(app_id, local_apk_path):
    '''
    gets the curent edit
    gets the curent apk
    gets the curent apk etag
    gets local apk file
    uploads local apk file
    '''
    edit_id, etag = editFull()
    log.info("replacing apk")
    ## Get replace APK URL
    get_all_apks_URL = BASE_URL + '/v1/applications/%s/edits/%s/apks' % (app_id, edit_id)
    all_apks_response = requests.get(get_all_apks_URL, headers=headers)
    apk_id = all_apks_response.json()[0]['id']

    APK_id_url = get_all_apks_URL + '/%s' % apk_id
    APK_id_response = requests.get(APK_id_url, headers=headers)
    # get the etag
    apk_etag = APK_id_response.headers.get('ETag')
    # get the replace url
    replace_apk_url = APK_id_url + '/replace'

    ## Open the apk file on your local machine
    try:
        log.info("Reading local apk file")
        local_apk = open(local_apk_path, 'rb').read()
    except:
        log.error("Error opening local apk file")
        sys.exit(1)
    
    all_headers = {
        'Content-Type': 'application/vnd.android.package-archive',
        'If-Match': apk_etag
    }
    all_headers.update(headers)
    replace_apk_response = requests.put(replace_apk_url, headers=all_headers, data=local_apk)
    log.info(replace_apk_response.json())

def getListing():
    edit_id, etag = editFull()
    get_listing_url = BASE_URL + '/v1/applications/%s/edits/%s/listings/en-US' % (app_id, edit_id)
    get_listing_response = requests.get(get_listing_url, headers=headers)
    log.info(get_listing_response)
    return get_listing_response.json()

def setListingRecentChanges(recent_changes):
    edit_id, etag = editFull()
    listing = getListing()
    listing['recentChanges'] = recent_changes
    set_listing_url = BASE_URL + '/v1/applications/%s/edits/%s/listings/en-US' % (app_id, edit_id)
    all_headers = {
        'If-Match': etag
    }
    all_headers.update(headers)
    set_listing_response = requests.put(set_listing_url, headers=all_headers, json=listing)
    log.info(set_listing_response)
    log.info(set_listing_response.json())

def submitCommit():
    edit_id, etag = editFull()
    log.info("getting open edit")
    commit_url = BASE_URL + '/v1/applications/%s/edits/%s/commit' % (app_id, edit_id)
    all_headers = {"if-match": etag}
    all_headers.update(headers)
    commit_response = requests.post(commit_url, headers=all_headers)
    log.info(commit_response.json())

def getAPKversion():
    '''
    Get the current APK version
    Compare to the the local build.gradle versioncode to see if update required
    '''
    create_edit_path = '/v1/applications/%s/edits' % app_id
    create_edit_url = BASE_URL + create_edit_path
    try:
        create_edit_response = requests.get(create_edit_url, headers=headers)
        edit_id = create_edit_response.json()['id']
    except:
        create_edit_response = requests.post(create_edit_url, headers=headers)
        edit_id = create_edit_response.json()['id']
    
    # Get the Open Edit
    get_apks_path = '/v1/applications/%s/edits/%s/apks' % (app_id, edit_id)
    get_apks_url = BASE_URL + get_apks_path
    apks = requests.get(get_apks_url, headers=headers)
    firstAPK = apks.json()[0]
    VersionCode = firstAPK['versionCode']
    return VersionCode

def getLocalVersion():
    # open the file for reading
    with open('app/build.gradle', 'r') as file:
        # loop through each line of the file
        for line in file:
            # check if the line contains "versionCode"
            if "versionCode" in line:
                # extract the version code from the line
                versionCode = line.split()[-1].strip('"\n')
                return int(versionCode)

if __name__ == '__main__':
    
    
    parser = argparse.ArgumentParser(description='Deploy app to Amazon Appstore')
    parser.add_argument('--run', required=False, action='store_true', help='Run the script')
    parser.add_argument('--client_id', required=True, help='client_id - you get this from amazon portal, apps&services ribbon, API access, Security profiles, Web Settings, looks like amzn1.application-oa2-client.XXX...')
    parser.add_argument('--client_secret', required=True, help='client_secret - you get this from amazon portal, apps&services ribbon, API access, Security profiles, Web Settings, this is a 64 char string')
    parser.add_argument('--app_id', required=True, help='app_id - you get this from amazon portal, apps&services ribbon, My app, App, App Submission API Keys, amzn1.devportal.mobileapp.XXX...')
    parser.add_argument('--local_apk_path', required=True, help='apk path on local computer. Example: d:/myapp.apk')
    parser.add_argument('--recent_changes', required=False, help='recent_changes. Example: "fixed bug xyz"')
    parser.add_argument('--verbose', required=False, action='store_true', help='verbose output')
    args = parser.parse_args()

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


    log.info("parsing arguments and assigning variables")
    
    client_id = args.client_id
    client_secret = args.client_secret
    app_id = args.app_id
    local_apk_path = args.local_apk_path
    recent_changes = args.recent_changes
    BASE_URL = 'https://developer.amazon.com/api/appstore'



    headers = authenticate(client_id, client_secret)
    local_versionCode = getLocalVersion()      
    online_current_versionCode = getAPKversion()

    if local_versionCode == online_current_versionCode:
        print("No update required, versions are the same")
        sys.exit(0)

    # continue the application
    APKreplace(app_id, local_apk_path)
    setListingRecentChanges(recent_changes)
    submitCommit()

