package sasuke;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sasuke.cards.ChidoriCard;
import sasuke.cards.DefaultAttackWithVariable;
import sasuke.cards.DefaultCommonAttack;
import sasuke.cards.DefaultCommonPower;
import sasuke.cards.DefaultCommonSkill;
import sasuke.cards.DefaultRareAttack;
import sasuke.cards.DefaultRarePower;
import sasuke.cards.DefaultRareSkill;
import sasuke.cards.DefaultSecondMagicNumberSkill;
import sasuke.cards.DefaultUncommonAttack;
import sasuke.cards.DefaultUncommonPower;
import sasuke.cards.DefaultUncommonSkill;
import sasuke.cards.FireBallCard;
import sasuke.cards.OrbSkill;
import sasuke.cards.SharinganCard;
import sasuke.characters.SasukeCharacter;
import sasuke.events.IdentityCrisisEvent;
import sasuke.potions.PlaceholderPotion;
import sasuke.relics.BottledPlaceholderRelic;
import sasuke.relics.DefaultClickableRelic;
import sasuke.relics.PlaceholderRelic;
import sasuke.relics.PlaceholderRelic2;
import sasuke.util.IDCheckDontTouchPls;
import sasuke.util.TextureLoader;
import sasuke.variables.DefaultCustomVariable;
import sasuke.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author xiaoqi
 */
@SpireInitializer
public class Sasuke implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {

    private static final Logger logger = LogManager.getLogger(Sasuke.class.getName());

    private static String modID;

    private static Properties theDefaultDefaultSettings = new Properties();

    private static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";

    private static boolean enablePlaceholder = true;

    //=========================游戏内设置, 不重要============================
    private static final String MOD_NAME = "PHARMACIST Mod";
    private static final String AUTHOR = "xq";
    private static final String DESCRIPTION = "PHARMACIST Mod";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // 角色颜色
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    //药水颜色
    private static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    private static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    private static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "sasukeResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "sasukeResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "sasukeResources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = "sasukeResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "sasukeResources/images/512/card_small_orb.png";

    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "sasukeResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "sasukeResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "sasukeResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "sasukeResources/images/1024/card_default_gray_orb.png";

    //todo 人物素材(人物选择界面、选择按钮)
    private static final String THE_DEFAULT_BUTTON = "sasukeResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "sasukeResources/images/charSelect/sasuke.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "sasukeResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "sasukeResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "sasukeResources/images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    private static final String BADGE_IMAGE = "sasukeResources/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "sasukeResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "sasukeResources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public Sasuke() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);

        setModID("sasuke");

        logger.info("Done subscribing");

        logger.info("Creating the color " + SasukeCharacter.Enums.COLOR_GRAY.toString());

        BaseMod.addColor(SasukeCharacter.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");

    }

    // ====== NO EDIT AREA ======

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = Sasuke.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = Sasuke.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = Sasuke.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        Sasuke sasuke = new Sasuke();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================

    /**
     * 加载角色
     */
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + SasukeCharacter.Enums.PHARMACIST.toString());

        BaseMod.addCharacter(new SasukeCharacter("sasuke", SasukeCharacter.Enums.PHARMACIST),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, SasukeCharacter.Enums.PHARMACIST);

        receiveEditPotions();
        logger.info("Added " + SasukeCharacter.Enums.PHARMACIST.toString());
    }

    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                }, // thing??????? idk
                (button) -> { // The actual button:

                    enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("sasuke", "theDefaultConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MOD_NAME, AUTHOR, DESCRIPTION, settingsPanel);


        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    // =============== / POST-INITIALIZE/ =================


    // ================ 添加药水 ===================

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.PHARMACIST".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class,
                PLACEHOLDER_POTION_LIQUID,
                PLACEHOLDER_POTION_HYBRID,
                PLACEHOLDER_POTION_SPOTS,
                PlaceholderPotion.POTION_ID,
                SasukeCharacter.Enums.PHARMACIST);

        logger.info("Done editing potions");
    }


    // ================ 添加遗物 ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), SasukeCharacter.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), SasukeCharacter.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), SasukeCharacter.Enums.COLOR_GRAY);

        // This adds a relic to the Shared pool. Every character can find this relic.
        BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        logger.info("Done adding relics!");
    }

    // ================ 添加卡牌 ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        logger.info("Adding cards");
        // Add the cards
        // Don't comment out/delete these cards (yet). You need 1 of each type and rarity (technically) for your game not to crash
        // when generating card rewards/shop screen items.

        BaseMod.addCard(new FireBallCard());
        BaseMod.addCard(new ChidoriCard());
        BaseMod.addCard(new SharinganCard());
        BaseMod.addCard(new OrbSkill());
        BaseMod.addCard(new DefaultSecondMagicNumberSkill());
        BaseMod.addCard(new DefaultCommonAttack());
        BaseMod.addCard(new DefaultAttackWithVariable());
        BaseMod.addCard(new DefaultCommonSkill());
        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultUncommonSkill());
        BaseMod.addCard(new DefaultUncommonAttack());
        BaseMod.addCard(new DefaultUncommonPower());
        BaseMod.addCard(new DefaultRareAttack());
        BaseMod.addCard(new DefaultRareSkill());
        BaseMod.addCard(new DefaultRarePower());

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.
        UnlockTracker.unlockCard(FireBallCard.ID);
        UnlockTracker.unlockCard(ChidoriCard.ID);
        UnlockTracker.unlockCard(SharinganCard.ID);
        UnlockTracker.unlockCard(OrbSkill.ID);
        UnlockTracker.unlockCard(DefaultSecondMagicNumberSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonAttack.ID);
        UnlockTracker.unlockCard(DefaultAttackWithVariable.ID);
        UnlockTracker.unlockCard(DefaultCommonSkill.ID);
        UnlockTracker.unlockCard(DefaultCommonPower.ID);
        UnlockTracker.unlockCard(DefaultUncommonSkill.ID);
        UnlockTracker.unlockCard(DefaultUncommonAttack.ID);
        UnlockTracker.unlockCard(DefaultUncommonPower.ID);
        UnlockTracker.unlockCard(DefaultRareAttack.ID);
        UnlockTracker.unlockCard(DefaultRareSkill.ID);
        UnlockTracker.unlockCard(DefaultRarePower.ID);

        logger.info("Done adding cards!");
    }

    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/Sasuke-Orb-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/Sasuke-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
