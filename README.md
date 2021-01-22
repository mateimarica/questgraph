![Logo](https://i.imgur.com/95Rji4r.png)

I noticed that Questrade (online brokerage firm) does not does show you any sort of history, graphs, or visuals of any kind to how your investment portfolios are performing. Thus, here is an Android application that does so using the [Questrade API](https://www.questrade.com/api).

Questrade uses a REST API, for access to read and write Questrade data and OAuth 2.0 as a security protocol.  

*This application is in the very early stages of development.*

<br/>

## Preview
![Screenshots](https://i.imgur.com/3ZhewHo.png) <sup>Note: The account # and the balances in the screenshots above are simulated and not real.<sup>
  
<br/>  

## Requirements
* A Questrade account with API access enabled
* Android 6.0+

<br/>

## Setup
*Insert setup here (allowing API access for Questrade, getting auth token, etc)*

<br/>

## Notes
* This application can only request and read your account data from Questrade. This data is stored locally in the app-specific storage, no data is uploaded to a 3rd-party server. However, data such as your account number, balance, positions are potentially accessible due to them (as of currently) being unencrypted. While not much could be arguably done with this information, use at your own discretion.

<br/>

## TODO
* Upload APKs to github
* Implement SQLite database management system
* Implement retroactive data gathering by tracking deposits, withdrawals, trades, etc. (current version can only create visuals using real-time data)
* Overhaul *Account Overview* page
* Overhaul *Home* page and show overall account balance