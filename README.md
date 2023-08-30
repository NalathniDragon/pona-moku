# pona moku
*food improvement - tasty boons - nourishing elegance - the joy of snacks*
-----
Minecraft's baseline food system is more chore than gameplay. This mod aims to fix that by getting rid of the vanilla hunger mechanics entirely, and instead making foods distinct by having them provide different amounts of healing, absorption, and status buffs. Eating a meal clears your food statuses and replaces them with a new set based on the ingredients of the food, so preparing different meals for different occasions is meaningful. Heartier foods give more absorption, but also take longer to eat, making it trickier to use them in combat; light foods can be chowed down quickly for fast healing but provide less protection on top of your health.

## Features
- Vanilla hunger mechanics are entirely replaced.
- Most vanilla foods give buffs when eaten, or can be cooked into food that does so. These are based on a config file, so you can change the buffs and add support for more foods if you want. Food buffs last until you next eat.
- All foods give health and absorption based on their nutritional value. (The scaling factor is configurable.)
- Foods take different amounts of time to eat based on their health/absorption stats. This is also configurable.
- Taking nontrivial damage interrupts your eating, so it might be useful to bring a diversity of foods to combat so you can sneak in healing snacks quickly or retreat and get a large absorption shield.
- Status effect changes: The Hunger status effect now reduces the hearts gained from food while you're afflicted with it. Conversely, Saturation boosts it. The Nausea status effect prevents you from eating for the duration.

## FAQ
**Is modded food supported?**
Yes and no. Food effects are currently specified in a config file, which can be modified to include non-vanilla foods. Healing and absorption for modded foods should work fine. In a future update I intend to do dynamic recipe examination in order to automatically support modded foods, giving them effects based on their ingredients. Modded edible blocks (i.e., cake-like) are not supported and would require writing custom code for each one, since they have their own implementations.
