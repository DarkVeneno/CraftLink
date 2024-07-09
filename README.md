![[MODRINTH PAGE]](https://modrinth.com/mod/craftlink)

![Here I use CraftLink's basic features: I use the search bar to open the Minecraft Wiki on the Browser.](https://cdn.modrinth.com/data/VmGDXrQb/images/5014f2a97ad6beafe147c978db911f2dc64ee40b.gif)

This is my first mod. I hope you like it.


![A CraftLink logo that consists of stylized pixel art text that says "CraftLink"](https://cdn.modrinth.com/data/cached_images/706ba7350e0e1b10e97f4dea741bcbc06054d687.png)

CraftLink is a mod that adds an integrated minimalistic web browser to your game, perfect for doing quick web searches while minecrafting.

## Simplicity

Simplicity is the main goal of this mod. It isn't as featureful as a full browser like Google Chrome or Safari, but the mod is meant for the game - the game isn't meant for the mod. CraftLink is a **tool**, not a gadget.

## Efficiency

It won't make you mine faster, but it will make you search faster. The mod includes an easily accessible Search Bar allowing you to quickly search for anything, or directly visit a website. There are intuitive shortcuts for zooming, scaling, and refreshing.



<details>
<summary>Features</summary>

_If you wish for a more detailed and/or technical description, visit the [wiki.](https://github.com/DarkVeneno/CraftLink/wiki)_

![Demonstration of the search bar. I search "Hello" on Google.](https://cdn.modrinth.com/data/VmGDXrQb/images/d06ecc29150f4661cc29717ef783041a00c66969.gif)
The Search Bar is a way to quickly search stuff on your preferred search engine. You can access it directly from in-game without entering the browser screen first with the Backspace key. From the browser screen access it with CTRL + Space.

![Demonstration of the search bar's URL function. I use it to go to google.com.](https://cdn.modrinth.com/data/VmGDXrQb/images/cff516150974732a96a702d132e84fad4a179971.gif)
You can also directly enter URLs in the Search Bar to directly visit websites. Just write '>' before your URL. By default, the Search Bar will always search whatever you write on your preferred search engine, even if it's a URL.

![I adjust the page's zoom level.](https://cdn.modrinth.com/data/VmGDXrQb/images/06c1152167a6423ea5f11c689d7a7b2b01eac8ab.gif)
Zoom in and out with CTRL + Scroll.

![I refresh the browser.](https://cdn.modrinth.com/data/VmGDXrQb/images/852850c463f129f0a90af0635be12ee712c3bc0e.gif)
Press CTRL + R to refresh.

![I return to the home page.](https://cdn.modrinth.com/data/VmGDXrQb/images/50b10654db5b2fe53f2c99d00a759f9c3679167e.gif)
CTRL + H will return you to the home page.

![I access the help window, which contains all of the browser's controls.](https://cdn.modrinth.com/data/VmGDXrQb/images/c878a98961159e52cd60b0cb3175bd9fb569fd82.gif)
Pressing CTRL + SHIFT will reveal the help window, with all of the shortcuts.

![I access the mod's configuration screen/settings screen.](https://cdn.modrinth.com/data/VmGDXrQb/images/f0c7952d0b71f59046092a023759dda58e74abac.gif)
CTRL + S will reveal the Settings / Config Screen.

![I adjust the scale/resolution of the page.](https://cdn.modrinth.com/data/VmGDXrQb/images/7ec1d6bcae87708e5dee7a56544281766689e563.gif)
You can change the page's scale with CTRL + (Up / Down arrow). This changes the resolution of the page. I recommend using zoom instead for most cases.

![I change the search engine from Google to Bing in the config screen, and make a Bing search.](https://cdn.modrinth.com/data/VmGDXrQb/images/e992a7b7023d8a031af33bc0eb43499b36757da4.gif)
You can change the default search engine! Here I changed it to Bing.

</details>

## Configurability + Performance

The heavy lifting is done with [MCEF](https://modrinth.com/mod/mcef/version/2.1.1-1.20.1), [owo-lib](https://modrinth.com/mod/owo-lib), and [MidnightLib](https://modrinth.com/mod/midnightlib), which are known and tested libraries, so it should ensure smooth performance without impacting your game.


<details>
<summary>So, what is configurable?</summary>

_The config screen also includes detailed explanations for the configurations._

- The home page URL. The home page is the page that is opened when you first open the browser in a session, or when you return to it with CTRL + H. - Google by default
  - You can choose one of the default choices (presets), Google, Bing, Yahoo, DuckDuckGo, Ecosia, and Ask, or you can use your own custom url for the home page.
- The default scale factor - 1.0 (100%) by default
- The search engine (which will be used by the search bar) - Google by default
  - You can choose one of the default choices (presets), Google, Bing, Yahoo, DuckDuckGo, Ecosia, and Ask, or you can use your own custom query url prefix for searches.
- "Activate Help Text Overlay". This activates an overlay text displaying basic keyboard shortcut controls for the browser, while you hold CTRL. - Off by default
- "Activate Loading Message". This activates a "Loading..." overlay text while the browser is loading a page. - Off by default.

</details>



## Other features

- CraftLink allows you to access the config screen through ModMenu.
- This mod opens links directly from in-game chat in the browser.
- A help overlay when pressing control (default off)
- A "Loading..." overlay when the browser is loading a page (default off)
- An easter egg in the configuration screen :)


<details>
<summary>Special note</summary>

_Note: The browser will **ALWAYS** try to load a page. If the page doesn't exist, it will display a blank page. This might change in the future, but I want to make sure the error detection algorithm is perfect and doesn't spit out an error where there isn't one. If this happens, press CTRL+SHIFT+S, and this will conveniently search the failed URL on your search engine of choice automatically._

</details>



## Documented

I wrote a [wiki](https://github.com/DarkVeneno/CraftLink/wiki) page, but the mod **itself includes a built-in help window**, accessible with [CTRL + SHIFT].

## Licensing

This mod is licensed under the [MIT License](https://github.com/DarkVeneno/CraftLink/blob/main/LICENSE).
