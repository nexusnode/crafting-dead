![Crafting Dead Logo](./image/crafting-dead-logo.png)  

[Now in English] [切换为简体中文](./README_zh_cn.md)  

Starting out as a mod for Minecraft 1.5, Crafting Dead has been around for many years now. It was built on F3RULLO's Gun Mod, one of the first gun mods on the Minecraft scene and has been growing ever since, constantly being enhanced with more content and features.

Crafting Dead adds in a multitude of zombies and guns, along with cosmetics and medical supplies. The guns are fully customisable with attachments and paints; they are competitive ready with accurate hit detection, latency compensation and recoil. Medical supplies include first aid kits, adrenaline and bandages enabling you to quickly escape, attack and recover from combat situations.

# Discord
![Discord Banner 2](https://discordapp.com/api/guilds/473735245636698153/widget.png?style=banner2)

# Contributing
Pull requests are always welcome, however please adhere to the following guidlines:
* Use the [Google style guide](https://github.com/google/styleguide)
* Add `@Override` annotations where appropriate to make overrides explicitly clear
* Rename semi-obfuscated variables (e.g. p_77624_1_) to meaningful names
* Turn on compiler warnings; resolve all of them (raw types, resource leaks, unused imports, unused variables etc.)
* Always use `this` keyword to make code as clear/readable as possible and to avoid ambiguous naming conflicts (which can go without notice)
* Always use curly braces `{}` around if statements to make them clearer and easily expandable, e.g. 
```java
if(foo) {
  bar();
}
```
instead of 
```java
if(foo)
  bar();
```

* Names of fields being used as constants should be all upper-case, with underscores separating words. The following are considered to be constants:
1. All `static final` primitive types (Remember that all interface fields are inherently static final).
2. All `static final` object reference types that are never followed by "`.`" (dot).
3. All `static final` arrays that are never followed by "`[`" (opening square bracket).

Examples of constants:
`MIN_VALUE, MAX_BUFFER_SIZE, OPTIONS_FILE_NAME`

Examples of non-constants:
`logger, clientConfig`

# License
Crafting Dead is licensed under LICENSE.txt for more information. You may use Crafting Dead in modpacks, reviews or any other medium as long as you obide by the terms of the license. Commercial use of the mod must be authorised by NEXUSNODE Directors (Brad Hunter).

Commercial use is any reproduction or purpose that is marketed, promoted, or sold and incorporates a financial transaction. You can use Crafting Dead for personal use, To host a server for friends for example, But not to use the mod or mod pack in any way that gives you a financial advantage, neither can you use or modify the code and re-sell it which will allow others to gain a financial advantage. Any use of the Crafting Dead mod other than within the official Crafting Dead Mod pack is for personal use, none of our work or assets can be used in any way that will allow the person commercial advantage or monetary compensation.

Any Youtubers or Twitch streamers are welcome to create videos using our mods and monetize them, However you will need to ensure that you link the official pack/mod in the video description. You are welcome to create your own pack using this mod without authrorisation as long as it is soley used as a private server and your finantial advantage comes soley from video monetization. 

Please contact brad@nexusnode.com or Brad#8888 on Discord if you have any questions or concerns. Commercial use through the official modpack is granted (Such as adding servers through the official modpack) anything that falls outside of the official modpack requires authrorisation. 

# Credit
- NEXUSNODE
- CakeBrains
- Sm0keySa1m0n
- Arzio
- F3RULLO14

# YourKit
YourKit supports open source projects with innovative and intelligent tools
for monitoring and profiling Java and .NET applications.
YourKit is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/), [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/), and [YourKit YouMonitor](https://www.yourkit.com/youmonitor/).

![YourKit Logo](https://www.yourkit.com/images/yklogo.png)
