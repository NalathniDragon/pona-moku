# pona moku

A mod to replace Minecraft's uninteresting hunger system with food that gives direct healing and useful buffs.

## Roadmap
### Core
- Remove baseline hunger mechanics
- Food provides healing based on its nourishment value
  - Heal amount scaled by config file
  - Tooltip shows health gain
- Food takes time to consume proportional to the healing it provides
  - Eating time scaled by config file
  - Taking damage interrupts eating
- Food provides an absorption-like overshield based on its saturation value
  - Shield scaled by config file
  - UI for presenting shield level, can be ugly initially
  - Intercept damage events to apply correctly to shield instead of health

### Food Buffs
- Food, when eaten, provides a buff for as long as its shield lasts
- Food buffs based on the food's ingredients (analyzed through Thaumcraft-like recipe tree examination and/or applying metadata upon food crafting)
  - intermediate step: make specific foods apply specific buffs, for testing
  - config-driven ingredient/buff association
  - tooltips show potential buffs for ingredients
  - By default, only the strongest ingredient effect is applied by the meal; combinations of ingredients can 'unlock' secondary effects by making that effect dominant
