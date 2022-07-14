[![Build Status](https://travis-ci.org/arkasandr/carfinesearcherbot.svg?branch=master)](https://travis-ci.org/arkasandr/carfinesearcherbot)
[![codecov](https://codecov.io/gh/arkasandr/study/branch/master/graph/badge.svg)](https://codecov.io/gh/arkasandr/carfinesearcherbot)

# carfinesearcherbot

My first introduction to Telegram API. 
I try to design service for searching information about car fines by entering car registration number and number of car registration certificate.
At the begining I see my service architecture as three component chain:
first section communicates with Telegram API and sends messages to second section, which is message brocker based on RabbitMQ, and the third section
is for extracting information from government resource.
Using RabbitMQ allow to build async service as it needs time to get information about car fines from datasource.
And one more interesting thing, there is no open API to get car fines, so third section uses playwright under the hood.
