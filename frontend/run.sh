#!/bin/bash

exec apache2ctl -e debug -DFOREGROUND >> /var/log/apache.log 2>&1