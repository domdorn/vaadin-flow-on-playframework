import Vaadin.Versions.{vaadinProgressBarVersion, vaadinRadioButtonVersion}
import sbt.librarymanagement.ModuleID

object Vaadin {

  object Versions {
    val projectVersion = "10.0.1"
  //  <flow.version>1.0.0</flow.version>
  val flowVersion = "1.0.0"
//    <flow.spring.version>10.0.0</flow.spring.version>
  val flowSpringVersion = "10.0.0"

//    <vaadin.button.version>1.0.0</vaadin.button.version>
  val vaadinButtonVersion = "1.0.0"
//    <vaadin.checkbox.version>1.0.0</vaadin.checkbox.version>
  val vaadinCheckboxVersion = "1.0.0"
//    <vaadin.combo.box.version>1.0.0</vaadin.combo.box.version>
  val vaadinComboBoxVersion = "1.0.0"
//    <vaadin.date.picker.version>1.0.0</vaadin.date.picker.version>
  val vaadinDatePickerVersion = "1.0.0"
//    <vaadin.dialog.version>1.0.0</vaadin.dialog.version>
  val vaadinDialogVersion = "1.0.0"
//    <vaadin.form.layout.version>1.0.0</vaadin.form.layout.version>
  val vaadinFormLayoutVersion = "1.0.0"
//    <vaadin.grid.version>1.0.0</vaadin.grid.version>
  val vaadinGridVersion = "1.0.0"
//    <vaadin.icons.version>1.0.0</vaadin.icons.version>
  val vaadinIconsVersion = "1.0.0"
//    <iron.list.version>1.0.0</iron.list.version>
  val ironListVersion = "1.0.0"
//    <vaadin.list.box.version>1.0.0</vaadin.list.box.version>
  val vaadinListBoxVersion = "1.0.0"
//    <vaadin.notification.version>1.0.0</vaadin.notification.version>
  val vaadinNotificationVersion = "1.0.0"
//    <vaadin.ordered.layout.version>1.0.0</vaadin.ordered.layout.version>
  val vaadinOrderedLayoutVersion = "1.0.0"
//    <vaadin.progress.bar.version>1.0.0</vaadin.progress.bar.version>
  val vaadinProgressBarVersion = "1.0.0"
//    <vaadin.radio.button.version>1.0.0</vaadin.radio.button.version>
  val vaadinRadioButtonVersion = "1.0.0"
//    <vaadin.split.layout.version>1.0.0</vaadin.split.layout.version>
  val vaadinSplitLayoutVersion = "1.0.0"
//    <vaadin.tabs.version>1.0.0</vaadin.tabs.version>
  val vaadinTabsVersion = "1.0.0"
//    <vaadin.text.field.version>1.0.0</vaadin.text.field.version>
  val vaadinTextFieldVersion = "1.0.0"
//    <vaadin.upload.version>1.0.0</vaadin.upload.version>
  val vaadinUploadVersion = "1.0.0"
//    <vaadin.board.version>2.0.0</vaadin.board.version>
  val vaadinBoardVersion = "2.0.0"
//    <vaadin.charts.version>6.0.0</vaadin.charts.version>
  val vaadinChartsVersion = "6.0.0"
//    <vaadin.testbench.version>6.0.0</vaadin.testbench.version>
  val vaadinTestbenchVersion = "6.0.0"
//    <vaadin.testbench.components.version>1.0.0</vaadin.testbench.components.version>
  val vaadinTestbenchComponentsVersion = "1.0.0"
//
//    <slf4j.version>1.7.25</slf4j.version>
  val slf4jVersion = "1.7.25"
  }

  object Dependencies {
    import Vaadin.Versions._
    private val org = "com.vaadin"

    val vaadin = ModuleID(org, "vaadin", projectVersion)
    val vaadinCore = ModuleID(org, "vaadin-core", projectVersion)
    val vaadinTestbench = ModuleID(org, "vaadin-testbench", projectVersion) // scope=test

    //    <!-- Flow -->
    val flow = ModuleID(org, "flow", flowVersion)
    val flowServer = ModuleID(org, "flow-server", flowVersion)
    val flowPush = ModuleID(org, "flow-push", flowVersion)
    val flowClient = ModuleID(org, "flow-client", flowVersion)
    val flowData = ModuleID(org, "flow-data", flowVersion)
    val vaadinLumoTheme = ModuleID(org, "vaadin-lumo-theme", flowVersion)
    val flowServerProductionMode = ModuleID(org, "flow-server-production-mode", flowVersion)

    val vaadinSpring = ModuleID(org, "vaadin-spring", flowSpringVersion)
    val vaadinSpringBootStarter = ModuleID(org, "vaadin-spring-boot-starter", projectVersion)
    val flowMavenPlugin = ModuleID(org, "flow-maven-plugin", flowVersion)

//    <!-- SLF4J -->
    val slf4j = ModuleID("org.slf4j", "slf4j-api", slf4jVersion)
    val slf4jSimple = ModuleID("org.slf4j", "slf4j-simple", slf4jVersion)
    val slf4jLog4J12 = ModuleID("org.slf4j", "slf4j-log4j12", slf4jVersion)
    val slf4jJdk14 = ModuleID("org.slf4j", "slf4j-jdk14", slf4jVersion)
    val slf4jJCL = ModuleID("org.slf4j", "slf4j-jcl", slf4jVersion)
//    <!-- Free Components -->
    val flowHtmlComponents = ModuleID(org, "flow-html-components", flowVersion)
    val vaadinButtonFlow = ModuleID(org, "vaadin-button-flow", vaadinButtonVersion)
    val vaadinCheckboxFlow = ModuleID(org, "vaadin-checkbox-flow", vaadinCheckboxVersion)
    val vaadinComboBoxFlow = ModuleID(org, "vaadin-combo-box-flow", vaadinComboBoxVersion)
    val vaadinDatePickerFlow = ModuleID(org, "vaadin-date-picker-flow", vaadinDatePickerVersion)
    val vaadinDialogFlow = ModuleID(org, "vaadin-dialog-flow", vaadinDialogVersion)
    val vaadinFormLayoutFlow = ModuleID(org, "vaadin-form-layout-flow", vaadinFormLayoutVersion)
    val vaadinGridFlow = ModuleID(org, "vaadin-grid-flow", vaadinGridVersion)
    val vaadinIconsFlow = ModuleID(org, "vaadin-icons-flow", vaadinIconsVersion)
    val vaadinIronListFlow = ModuleID(org, "vaadin-iron-list-flow", ironListVersion)
    val vaadinListBoxFlow = ModuleID(org, "vaadin-list-box-flow", vaadinListBoxVersion)
    val vaadinNotificationFlow = ModuleID(org, "vaadin-notification-flow", vaadinNotificationVersion)
    val vaadinOrderedLayoutFlow = ModuleID(org, "vaadin-ordered-layout-flow", vaadinOrderedLayoutVersion)
    val vaadinProgressBarFlow = ModuleID(org, "vaadin-progress-bar-flow", vaadinProgressBarVersion)
    val vaadinRadioButtonFlow = ModuleID(org, "vaadin-radio-button-flow", vaadinRadioButtonVersion)
    val vaadinSplitLayoutFlow = ModuleID(org, "vaadin-split-layout-flow", vaadinSplitLayoutVersion)
    val vaadinTabsFlow = ModuleID(org, "vaadin-tabs-flow", vaadinTabsVersion)
    val vaadinTextFieldFlow = ModuleID(org, "vaadin-text-field-flow", vaadinTextFieldVersion)
    val vaadinUploadFlow = ModuleID(org, "vaadin-upload-flow", vaadinUploadVersion)

    //
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-testbench-bom</artifactId>
//        <version>${vaadin.testbench.version}</version>
//        <type>pom</type>
//        <scope>import</scope>
//      </dependency>
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>flow-html-components-testbench</artifactId>
//        <version>${flow.version}</version>
//        <scope>test</scope>
//      </dependency>
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-components-testbench</artifactId>
//        <version>${vaadin.testbench.components.version}</version>
//        <scope>test</scope>
//      </dependency>
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-charts-testbench</artifactId>
//        <version>${vaadin.charts.version}</version>
//        <scope>test</scope>
//      </dependency>
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-board-testbench</artifactId>
//        <version>${vaadin.board.version}</version>
//        <scope>test</scope>
//      </dependency>
//
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-charts-flow</artifactId>
//        <version>${vaadin.charts.version}</version>
//      </dependency>
//      <dependency>
//        <groupId>com.vaadin</groupId>
//        <artifactId>vaadin-board-flow</artifactId>
//        <version>${vaadin.board.version}</version>
//      </dependency>
//
//    <!-- Transitive webjar dependencies, defined here for repeatable
//                builds -->
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-icon</artifactId>
//        <version>2.1.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-flex-layout</artifactId>
//        <version>2.0.3</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-meta</artifactId>
//        <version>2.1.1</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-iconset-svg</artifactId>
//        <version>2.2.1</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-a11y-announcer</artifactId>
//        <version>2.1.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-overlay-behavior</artifactId>
//        <version>2.3.4</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-fit-behavior</artifactId>
//        <version>2.2.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-a11y-keys-behavior</artifactId>
//        <version>2.1.1</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-media-query</artifactId>
//        <version>2.1.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-scroll-target-behavior</artifactId>
//        <version>2.1.1</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymer</groupId>
//        <artifactId>polymer</artifactId>
//        <version>2.6.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.webcomponents</groupId>
//        <artifactId>shadycss</artifactId>
//        <version>1.2.1</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.webcomponents</groupId>
//        <artifactId>webcomponentsjs</artifactId>
//        <version>1.2.0</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.polymerelements</groupId>
//        <artifactId>iron-resizable-behavior</artifactId>
//        <version>2.1.0</version>
//      </dependency>
//
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-control-state-mixin</artifactId>
//        <version>2.0.3</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-overlay</artifactId>
//        <version>3.0.3</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-themable-mixin</artifactId>
//        <version>1.1.6</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-element-mixin</artifactId>
//        <version>1.0.2</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-usage-statistics</artifactId>
//        <version>1.0.8</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-development-mode-detector</artifactId>
//        <version>1.0.3</version>
//      </dependency>
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>license-checker</artifactId>
//        <version>2.0.1</version>
//      </dependency>
//
//      <dependency>
//        <groupId>org.webjars.bowergithub.vaadin</groupId>
//        <artifactId>vaadin-list-mixin</artifactId>
//        <version>2.0.0</version>
//      </dependency>
//    </dependencies>

  }


}
