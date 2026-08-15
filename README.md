# Tamable Fox
## Features
- [x] Breeding two fox leads to new fox become tammed
- [x] Sit/Stand
- [x] Follow Owner
- [x] Guard Mode (If owner get targetd by another mob then attack the mob on sitting position)
- [x] While Guard Mode return to guard position after Combat
- [x] Fast Follow (when 20 blocks away from owner or more)
- [x] Teleport (When too much far away)
- [x] Give Item and make them immune to the player attack (who gave them the item)
- [x] If a player give an item to a fox then fox never drop it, unless player Sneak + Right Click to collect it
- [x] Tammed fox won't attack Chicken, Rabbits, Baby Turtle, Cod and Salmon
- [x] If player feed fox, if the health is not full, then the fox will restore health according to the saturation of the food (round up), and if the fox picks up any food, and saw health is not full, then it will eat the food in 3 seconds, rather than 30 seconds. 
- [x] If health is less than 25%, then it will go in sleeping animation, and heal until health recover more than 75% (All mobs will ignore the fox in this time)
- [x] Wild wolf and Polar Bear won't get angry on tammed Fox
- [ ] When on COMBAT mode when it will find any weapon the ground that is more powerful then current holding weapon, then it will pick it up, and store the previous item in it's hidden inventory (If it was owner item), and attack the mob using this
- [ ] When a mob (entity) is attacking the fox first time, the first damage would do only 20% of actual damage, and if the fox is currently on running state then then it would take only 50% of incoming damage, both are stackable
- [ ] When a mob will get interested in the fox then the fox will run to the opposite of the player direction (Preferred direction where player is not faced), with zig zag style movement, during this movement the fox speed would be [interested_mob_speed * 2]
