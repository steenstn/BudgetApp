/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package budgetapp.main;

public final class R {
    public static final class array {
        public static final int months_array=0x7f060000;
    }
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
        public static final int categories_spinner=0x7f090003;
        public static final int dialog_category_name=0x7f09000f;
        public static final int dialog_choose_category_listview=0x7f090011;
        public static final int dialog_choose_category_textview=0x7f090010;
        public static final int dialog_dailybudget=0x7f090015;
        public static final int dialog_dailybudget_currentbudget=0x7f090014;
        public static final int dialog_other_category_comment=0x7f090013;
        public static final int dialog_other_category_name=0x7f090012;
        public static final int editTextSubtract=0x7f090002;
        public static final int fullscreen_content=0x7f090000;
        public static final int menu_addcategory=0x7f090017;
        public static final int menu_removecategory=0x7f090018;
        public static final int menu_setdailybudget=0x7f090019;
        public static final int menu_statistics=0x7f09001a;
        public static final int menu_undo=0x7f090016;
        public static final int spinnerCategory=0x7f09000b;
        public static final int spinnerMonth=0x7f09000a;
        public static final int spinnerYear=0x7f090009;
        public static final int textViewCurrentBudget=0x7f090001;
        public static final int textViewLogLeft=0x7f090007;
        public static final int textViewLogRight=0x7f090008;
        public static final int textViewLogStats=0x7f09000c;
        public static final int textViewLogTop=0x7f09000e;
        public static final int textViewLogTopHead=0x7f09000d;
        public static final int topCategoryButton1=0x7f090005;
        public static final int topCategoryButton2=0x7f090004;
        public static final int topCategoryButton3=0x7f090006;
    }
    public static final class layout {
        public static final int activity_graph=0x7f030000;
        public static final int activity_main=0x7f030001;
        public static final int activity_stats=0x7f030002;
        public static final int dialog_add_category=0x7f030003;
        public static final int dialog_choose_category=0x7f030004;
        public static final int dialog_other_category=0x7f030005;
        public static final int dialog_remove_category=0x7f030006;
        public static final int dialog_set_dailybudget=0x7f030007;
    }
    public static final class menu {
        public static final int activity_main=0x7f080000;
    }
    public static final class string {
        public static final int all_categories=0x7f05000c;
        public static final int all_months=0x7f05000b;
        public static final int all_years=0x7f05000a;
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
        public static final int AppBaseTheme=0x7f070000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f070001;
        public static final int ButtonBar=0x7f070003;
        public static final int ButtonBarButton=0x7f070002;
        public static final int FullscreenActionBarStyle=0x7f070005;
        public static final int FullscreenTheme=0x7f070004;
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
