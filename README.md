![](https://img.shields.io/badge/android-API%20%2023%2B-green)
![GitHub last commit](https://img.shields.io/github/last-commit/ItalianCoders/mybudget2-mobile-android)
![GitHub issues](https://img.shields.io/github/issues-raw/ItalianCoders/mybudget2-mobile-android?color=red)
![GitHub closed issues](https://img.shields.io/github/issues-closed/ItalianCoders/mybudget2-mobile-android)
[![CircleCI](https://circleci.com/gh/ItalianCoders/mybudget2-mobile-android.svg?style=shield)](https://14-191944532-gh.circle-artifacts.com/0/reports/tests/testReleaseUnitTest/index.html)

# :moneybag: My Budget

## :pencil: Introduzione
Grazie alla comunità degli autori di [italianCoders](https://italiancoders.it) nasce My Budget, un progetto opensource, mantenuto dagli stessi che si propone di raggiungere i seguenti obiettivi:

1. Aumentare il bagaglio di conoscenze degli autori tramite lo scambio di idee, abilità ed esperienze.

2. Condividere con la numerosa comunità di programmatori che ruota attorno a italianCoders un esempio di quali tecniche di programmazione e framework utilizzare e in che modo per offrire un aiuto attraverso il codice presente o con domande agli stessi autori.

3. Ultimo ma non meno importante, quello di offrire un'applicazione gratuita e pratica che possa trovare un utilizzo concreto nella vita di tutti i giorni.

## :black_nib: Autori
FrontEnd Android: [Gianluca Fattarsi](https://gianlucafattarsi.github.io/)

BackEnd: [Dario Frongillo](http://dariofrongillo.com)

## :iphone: My Budget Android
Linguaggio utilizzato: Kotlin.

Questo repository ospita il codice del frontend del progetto mentre il backend lo si può trovare a [questo indirizzo](https://github.com/ItalianCoders/mybudget2-api).


## :microscope: Architettura
Di seguito verrà riportato un elenco delle tecniche di programmazione e framework utilizzati per realizzare l'applicazione.

- [DataBinding](#heavy_check_mark-databinding)
- [ViewModel](#heavy_check_mark-viewmodel)
- [LiveData](#livedata)
- [Observable](#observable)
- [Animation](#heavy_check_mark-animation)
- [Kotlin](#heavy_check_mark-kotlin)
- [Dagger injection](#dagger-injection)
- [REST client](#rest-client)
- [Tutorials](#heavy_check_mark-tutorials)
- [RecyclerView](#recyclerview)
- [State Machine](#heavy_check_mark-state-machine)
- [Activity startActivityForResult](#activity-startactivityforresult)
- [BottomSheetBehavior](#bottomsheetbehavior)
- [Firebase](#firebase)
- [Crashlytics](#crashlytics)
- [Settings](#settings)
- [Launch screen](#heavy_check_mark-launch-screen)
- [Room ORM](#room-orm)
- [Menu](#menu)
- [Local Junit Test](#local-junit-test)
- [Instrumented Test](#instrumented-test)
- [Shared test](#shared-test)

## :heavy_check_mark: DataBinding

- #### One-way / Two-way
  File: [activity_registration_user_info.xml](app/src/main/res/layout/activity_registration_user_info.xml)<br><br>Il _FrameLayout_ con id _sign_up_button_ binda le sue proprietà _background_ e _clickable_ alla proprietà dataValid del ViewModel

  Il _TextInputEditText_ con id _firstname_ET_ binda la proprietà _text_ alla proprietà firstname del ViewModel. Utilizzando <kbd>@={model.firstname}</kbd> il binding viene fatto in Two-way quindi scrivendo del testo nella vista si aggiorna anche la proprietà del ViewModel e viceversa

- #### Converters
  Directory: [converters](app/src/main/java/it/italiancoders/mybudget/databinding/converters)

  Un esempio è possibile trovarlo in [list_item_movement.xml](app/src/main/res/layout/list_item_movement.xml) in cui è stato aggiunto l'import nel tag data di [AmountStringConversion](app/src/main/java/it/italiancoders/mybudget/databinding/converters/AmountStringConversion.kt) e utilizzato per visualizzare il totale del documento.

  Il converter è stato creato per visualizzare l'importo del documento formattato secondo le impostazioni di sistema con il simbolo della valuta impostato nelle settings dell'applicazione.

- #### Adapters
  Directory: [adapters](app/src/main/java/it/italiancoders/mybudget/databinding/adapters)

  Nel layout [activity_registration_user_info.xml](app/src/main/res/layout/activity_registration_user_info.xml) viene utilizzato l'adapter [ValidationBindingAdapter](app/src/main/java/it/italiancoders/mybudget/databinding/adapters/ValidationBindingAdapter.kt) su tutti i _TextInputLayout_.

  Le proprietà che vengono prese in considerazione sono <kbd>app:validation</kbd> che rappresenta la rule da applicare per la validazione e <kbd>app:errorMsg</kbd> che rappresenta il messaggio di errore che verrà impostato se la rule non viene rispettata.

## :heavy_check_mark: ViewModel

- #### ViewModel
  I ViewModel presenti sono utilizzati in activity, fragment e custom view all'interno di tutta l'applicazione. Di seguito viene riportato un esempio per ogni tipologia.

  Activity: [RegistrationUserInfoActivity](app/src/main/java/it/italiancoders/mybudget/activity/registration/RegistrationUserInfoActivity.kt)

  Fragment: [ListMovementsFragment](app/src/main/java/it/italiancoders/mybudget/activity/movements/list/ListMovementsFragment.kt)

  Custom view: [SearchMovementsView](app/src/main/java/it/italiancoders/mybudget/activity/movements/search/SearchMovementsView.kt) |

- #### ViewModelFactory
  In Android non esiste la possibilità di poter instanziare un ViewModel con costruttore con 1 o più parametri attraverso un _ViewModelProviders_. Per questo motivo si rende necessario l'uso di _ViewModelFactory_.

  Nell'applicazione possiamo trovare l'uso di factory quasi ovunque. Un esempio può essere quello in [CategoriesActivity](app/src/main/java/it/italiancoders/mybudget/activity/categories/CategoriesActivity.kt) dove il ViewModel viene creato nel metodo _onCreate_ in questo modo

  ```java
  ViewModelProvider(this,CategoriesViewModelFactory(categoriesManager)).get(CategoriesViewModel::class.java)
  ```

##### LiveData
:construction: In costruzione :construction:

##### Observable
:construction: In costruzione :construction:

## :heavy_check_mark: Animation

- #### ValueAnimator
  Esempio: [LoginActivity](app/src/main/java/it/italiancoders/mybudget/activity/login/LoginActivity.kt)

  Le animaizoni utilizzatate nell'activity di login sono 2 e, al click del pulsante di login, si occupano rispettivamente di:
  - Ridurre la larghezza del pulsante
  - Cambiare la trasparenza del testo del pulsante e alla fine visualizzare la progress bar

  Prendendo come esempio la prima animazione ecco il codice:
  ```java
  val finalWidth = resources.getDimension(R.dimen.sign_button_loading_width).toInt()
  val anim = ValueAnimator.ofInt(binding.signInButton.measuredWidth, finalWidth)
  anim.addUpdateListener {
      val value: Int = it.animatedValue as Int
      val layoutParam = binding.signInButton.layoutParams
      layoutParam.width = value
      binding.signInButton.requestLayout()
  }
  anim.duration = 250
  anim.start()
  ```
  Il ValueAnimator si basa su un range di valori dalla normale larghezza del pulsante a quella finale (finalWidth) definita nelle resources. Il listener aggiunto contiene il valore corrente dell'animazione (it.animatedValue) che viene utilizzato per settare la nuova larghezza al layout del pulsante (layoutParam.width).

  Per altri esempi sull'uso di animazioni con ValueAnimator o utilizzando un ConstraintLayout è possibile consultare i miei articoli a questi indirizzi:

  [Android: creare favolose animazioni con ConstraintLayout – Parte 1](https://italiancoders.it/android-creare-favolose-animazioni-con-constraintlayout-parte-1)

  [Android: creare favolose animazioni con ConstraintLayout – Parte 2](https://italiancoders.it/android-creare-favolose-animazioni-con-constraintlayout-parte-2)

## :heavy_check_mark: Kotlin

- #### Extensions
  Per approfondire: [documentazione kotlinlang](https://kotlinlang.org/docs/reference/extensions.html)

  Kotlin permette di aggiungere nuove funzionalità ad una classe senza doverla estendere tramite delle speciali dichiarazioni chiamate appunto _Kotlin extensions_.

  Un esempio è possibile trovarlo nella classe [ViewModelExtensions](app/src/main/java/it/italiancoders/mybudget/utils/ViewModelExtensions.kt) in cui vengono aggiunti 3 nuovi metodi alla classe _ViewModel_: <kbd>uiJob</kbd>, <kbd>ioJob</kbd> e <kbd>backgroundJob</kbd>.

  Nella classe [LoginViewModel](app/src/main/java/it/italiancoders/mybudget/activity/login/LoginViewModel.kt) nel metodo _login_ troviamo l'uso dei primi 2 metodi citati in quanto chiunque estenda _ViewModel_ potrà averli a disposizione.

  Prendendo come riferimento Java tutto ciò sarebbe stato possibile realizzarlo creando una classe (astratta) _BaseViewModel_ che estende _ViewModel_ in cui implementare i 3 metodi e successivamente far estendere da questa classe _LoginViewModel_.

- #### Coroutines
  Per approfondire: [documentazione kotlinlang](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)

  Il capitolo delle coroutines in kotlin è molto vasto e prima di utilizzarle è assolutamente obbligatorio leggerne la documentazione. L'uso in questa applicazione per ora si limita alla gestione dei thread come quando succedeva in Java con l'utilizzo degli _AsyncTask_.

  Un esempio di utilizzo nell'applicazione può essere quello nella classe [RegistrationUserInfoManager](app/src/main/java/it/italiancoders/mybudget/manager/registrationuserinfo/RegistrationUserInfoManager.kt) nel metodo <kbd>resend</kbd>. In questo caso specifico viene eseguita la chiamata rest in background con
  ```java
  CoroutineScope(Dispatchers.IO).launch {}
  ```
  e la response viene processata con
  ```java
  withContext(Dispatchers.Main) {}
  ```

  Il tutto risulta
  ```java
  CoroutineScope(Dispatchers.IO).launch {
      // Operazioni in background
      val response = registrationUserInfoRestService.resend(userName)
      withContext(Dispatchers.Main) {
          // Operazioni su UI thread
          processResponse(response, onSuccessAction, onFailureAction)
      }
  }
  ```

##### Dagger injection
:construction: In costruzione :construction:
- Application
- Module
- Components

##### REST client
:construction: In costruzione :construction:
- Client
- Interceptor

## :heavy_check_mark: Tutorials

I tutorials presenti nelle varie activity dell'applicazione sono stati realizzati utilizzando la libreria [Spotlight](https://github.com/TakuSemba/Spotlight).

La [BaseActivity](app/src/main/java/it/italiancoders/mybudget/activity/BaseActivity.kt) comprende il metodo <kbd>createTutorial()</kbd> che è possibile sovrascrivere per far restituire il tutorial da visualizzare all'apertura dell'activity.

Ogni tutorial estende da [AbstractTutorialActivity](app/src/main/java/it/italiancoders/mybudget/tutorial/AbstractTutorialActivity.kt) in cui sono presenti i metodi per aprirlo, controllare se è già stato visualizzato e marcarlo come già visto utilizzando un file di preference dedicato (_tutorial_prefs_).

Nelle preference dell'applicazione è presente inoltre la possibilità di effettuare un reset dei tutorials per poterli rivedere anche in un secondo momento. Il tutto è gestito dalla classe [PrefResetTutorialHandler](app/src/main/java/it/italiancoders/mybudget/activity/settings/preferences/PrefResetTutorialHandler.kt).

##### RecyclerView
:construction: In costruzione :construction:
- PagerSnapHelper
- Adapter
- Item click listener

## :heavy_check_mark: State Machine

L'uso di una _State Machine_ in Informatica rientra in uno dei pattern più usati tradizionalmente. Non si tratta di un pattern _coding oriented_, quelli per internderci che fanno parte dei tipi della Gang of Four, ma _system oriented_.

In questa applicazione possiamo trovare un esempio nella classe [PeriodType](app/src/main/java/it/italiancoders/mybudget/activity/main/PeriodType.kt). Il metodo <kbd>nextType()</kbd> resituisce lo stato successivo a cui quello attuale può portare.

Nel caso specifico dell'applicazione la successione degli stati è infinita in questo modo:<br>MONTH --> WEEK --> DAY --> MONTH --> WEEK --> ecc...

##### Activity startActivityForResult
:construction: In costruzione :construction:

##### BottomSheetBehavior
:construction: In costruzione :construction:

##### Firebase
:construction: In costruzione :construction:

##### Crashlytics
:construction: In costruzione :construction:

##### Settings
:construction: In costruzione :construction:
- Summary
- Custom preference
- Custom file

## :heavy_check_mark: Launch screen

La creazione della launch screen è stata fatta con l'utilizzo di un tema dedicato impostato sull'activity di partenza.

Nel file [styles.xml](app/src/main/res/values/styles.xml) è presente il tema _AppTheme.Launcher_ seguente:
```xml
<style name="AppTheme.Launcher">
  <item name="android:windowBackground">@drawable/launcher_screen_with_logo</item>
  <item name="android:windowNoTitle">true</item>
  <item name="android:windowActionBar">false</item>
  <item name="android:windowFullscreen">true</item>
  <item name="android:windowContentOverlay">@null</item>
</style>
```
Nel [Manifest.xml](app/src/main/AndroidManifest.xml) il tema è stato impostato alla main activity
```xml
<activity
  android:name=".activity.main.MainActivity"
  android:label="@string/app_name"
  android:screenOrientation="portrait"
  android:theme="@style/AppTheme.Launcher">
  <intent-filter>
      <action android:name="android.intent.action.MAIN" />
      <category android:name="android.intent.category.LAUNCHER" />
  </intent-filter>
</activity>
```

##### Room ORM
:construction: In costruzione :construction:
- Database
- Entity
- Dao
- Converter

##### Menu
:construction: In costruzione :construction:
- Global
- Activity

##### Local Junit Test
:construction: In costruzione :construction:

##### Instrumented Test
:construction: In costruzione :construction:

##### Shared test
:construction: In costruzione :construction:
