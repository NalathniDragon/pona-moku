# pona moku

A mod to replace Minecraft's uninteresting hunger system with food that gives direct healing and useful buffs.

## Roadmap
### Core
- Remove baseline hunger mechanics - mixins in `HungerManager` and `FoodComponent`
- ✔Food provides healing based on its nourishment value
  - ✔Heal amount scaled by config file
  - ✔Tooltip shows health gain
- ✔Food takes time to consume proportional to the healing it provides - mixin `Item.getMaxUseTime`
  - ✔Eating time scaled by config file
  - Taking damage interrupts eating - [Fabrication reference](https://github.com/FalsehoodMC/Fabrication/blob/3.0/1.20.1/src/main/java/com/unascribed/fabrication/mixin/f_balance/interrupting_damage/MixinLivingEntity.java)
- ✔Food provides absorption based on its saturation value
  - ✔scaled by config file

### Food Buffs
- ✔Food, when eaten, provides a buff until you next eat
  - ✔Eating another food replaces the shield
- Food buffs based on the food's ingredients (analyzed through Thaumcraft-like recipe tree examination and/or applying metadata upon food crafting)
  - ✔intermediate step: make specific foods apply specific buffs, for testing
  - ✔tooltip shows buff provided by food
  - ✔config-driven ingredient/buff association
  - ✔tooltips show potential buffs for ingredients
### Integration
- Support food prepared by unattended crafting, e.g. by Create machines
- Support modded recipe types, e.g. Farmer's Delight cooking
- Support dynamic foods, e.g. Sandwiches
- Integration with planned future armor augments, may or may not be part of this mod
