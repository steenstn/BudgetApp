/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package budgetapp.main;

public final class R {
    public static final class attr {
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int buttonBarButtonStyle=0x7f010001;
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int buttonBarStyle=0x7f010000;
    }
    public static final class color {
        public static final int black_overlay=0x7f040000;
    }
    public static final class drawable {
        public static final int ic_launcher=0x7f020000;
    }
    public static final class id {
        public static final int categories_spinner=0x7f080005;
        public static final int dialog_category_name=0x7f08000f;
        public static final int dialog_dailybudget=0x7f080012;
        public static final int dialog_dailybudget_currentbudget=0x7f080011;
        public static final int dialog_other_category_name=0x7f080010;
        public static final int dummy_button=0x7f080002;
        public static final int editTextSubtract=0x7f080004;
        public static final int fullscreen_content=0x7f080000;
        public static final int fullscreen_content_controls=0x7f080001;
        public static final int menu_addcategory=0x7f080014;
        public static final int menu_removecategory=0x7f080015;
        public static final int menu_setdailybudget=0x7f080016;
        public static final int menu_statistics=0x7f080017;
        public static final int menu_undo=0x7f080013;
        public static final int textViewCurrentBudget=0x7f080003;
        public static final int textViewLogBottom=0x7f08000e;
        public static final int textViewLogBottomHead=0x7f08000d;
        public static final int textViewLogLeft=0x7f080006;
        public static final int textViewLogMiddle=0x7f08000c;
        public static final int textViewLogMiddleHead=0x7f08000b;
        public static final int textViewLogRight=0x7f080007;
        public static final int textViewLogStats=0x7f080008;
        public static final int textViewLogTop=0x7f08000a;
        public static final int textViewLogTopHead=0x7f080009;
    }
    public static final class layout {
        public static final int activity_graph=0x7f030000;
        public static final int activity_main=0x7f030001;
        public static final int activity_stats=0x7f030002;
        public static final int dialog_add_category=0x7f030003;
        public static final int dialog_other_category=0x7f030004;
        public static final int dialog_remove_category=0x7f030005;
        public static final int dialog_set_dailybudget=0x7f030006;
    }
    public static final class menu {
        public static final int activity_main=0x7f070000;
    }
    public static final class string {
        public static final int app_name=0x7f050000;
        public static final int dummy_button=0x7f050007;
        public static final int dummy_content=0x7f050006;
        public static final int menu_addcategory=0x7f050002;
        public static final int menu_logdata=0x7f050001;
        public static final int menu_removecategory=0x7f050003;
        public static final int menu_setdailybudget=0x7f050005;
        public static final int menu_statistics=0x7f050009;
        public static final int menu_undo=0x7f050004;
        public static final int title_activity_graph=0x7f050008;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.

    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.

        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.

    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f060000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f060001;
        public static final int ButtonBar=0x7f060003;
        public static final int ButtonBarButton=0x7f060002;
        public static final int FullscreenActionBarStyle=0x7f060005;
        public static final int FullscreenTheme=0x7f060004;
    }
    public static final class styleable {
        /** 
         Declare custom theme attributes that allow changing which styles are
         used for button bars depending on the API level.
         ?android:attr/buttonBarStyle is new as of API 11 so this is
         necessary to support previous API levels.
    
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_buttonBarButtonStyle budgetapp.main:buttonBarButtonStyle}</code></td><td></td></tr>
           <tr><td><code>{@link #ButtonBarContainerTheme_buttonBarStyle budgetapp.main:buttonBarStyle}</code></td><td></td></tr>
           </table>
           @see #ButtonBarContainerTheme_buttonBarButtonStyle
           @see #ButtonBarContainerTheme_buttonBarStyle
         */
        public static final int[] ButtonBarContainerTheme = {
            0x7f010000, 0x7f010001
        };
        /**
          <p>This symbol is the offset where the {@link budgetapp.main.R.attr#buttonBarButtonStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name android:buttonBarButtonStyle
        */
        public static final int ButtonBarContainerTheme_buttonBarButtonStyle = 1;
        /**
          <p>This symbol is the offset where the {@link budgetapp.main.R.attr#buttonBarStyle}
          attribute's value can be found in the {@link #ButtonBarContainerTheme} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name android:buttonBarStyle
        */
        public static final int ButtonBarContainerTheme_buttonBarStyle = 0;
    };
}
