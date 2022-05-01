# ScoreboardTool

## About

This is spigot plugin that make it easy to manage scoreboard.

## Installation

ggrks

## Spaces

Minecraft's command does not accept spaces.  
This plugin represents whitespace by replacing escape sequences with whitespace.  

`Escape Sequence(Whitespace): /s`

## Variable

Variable can use in score's entry or scoreboard title.  
This has mutable value and updates value at regular intervals.  
(The Interval is configurable in config.yml)

### Implemented

The following can be used variables.


### Not Implemented

The following is developing now. cannot be used.

%player_name% : player's name  
%player_display_name% : player's display name  
%player_custom_name% : player's custom name  
%player_location% : player's location  
%player_location_x% : player's x coordinate  
%player_location_y% : player's y coordinate  
%player_location_z% : player's z coordinate  
%player_direction% : player's facing direction  
%player_ping% : player's ping  

%server_address% : server address. configurable from config.yml  
%server_name% : server name configurable from config.yml

%date% : Real Date  
%mc_date% : MC Date
