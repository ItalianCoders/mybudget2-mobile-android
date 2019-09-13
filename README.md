![](https://img.shields.io/badge/android-API%20%2023%2B-green)
![GitHub last commit](https://img.shields.io/github/last-commit/ItalianCoders/mybudget2-mobile-android)
![GitHub issues](https://img.shields.io/github/issues-raw/ItalianCoders/mybudget2-mobile-android?color=red)
![GitHub closed issues](https://img.shields.io/github/issues-closed/ItalianCoders/mybudget2-mobile-android)
[![CircleCI](https://circleci.com/gh/ItalianCoders/mybudget2-mobile-android.svg?style=shield)](https://14-191944532-gh.circle-artifacts.com/0/reports/tests/testReleaseUnitTest/index.html)

# :moneybag: My Budget

## :pencil: Introduzione
Grazie alla comunità degli autori di [italianCoders](https://italiancoders.it) nasce My Budget, un progetto opensource, mantenuto dagli stessi che si propone di raggiungere i seguenti obiettivi:

1. Aumentare il bagaglio di conoscenze degli stessi autori tramite lo scambio di idee, abilità ed esperienze

2. Condividere con la numerosa comunità di programmatori che ruota attorno a italianCoders un esempio di quali tecniche di programmazione e framework utilizzare e in che modo per offrire un aiuto attraverso il codice presente o con domande agli stessi autori

3. Ultimo ma non meno importante, quello di offrire un'applicazione pratica che possa trovare un utilizzo concreto nella vita di tutti i giorni

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
- [Animation](#animation)
- [Kotlin coroutine](#kotlin-coroutine)
- [Dagger injection](#dagger-injection)
- [REST client](#rest-client)
- [Tutorial](#tutorial)
- [RecyclerView](#recyclerview)
- [State Machine Pattern](#state-machine-pattern)
- [Activity startActivityForResult](#activity-startactivityforresult)
- [BottomSheetBehavior](#bottomsheetbehavior)
- [Firebase](#firebase)
- [Crashlytics](#crashlytics)
- [Settings](#settings)
- [Splash screen](#splash-screen)
- [Room ORM](#room-orm)
- [Menu](#menu)
- [Network state monitor](#network-state-monitor)
- [Local Junit Test](#local-junit-test)
- [Instrumented Test](#instrumented-test)
- [Shared test](#shared-test)

## :heavy_check_mark: DataBinding

| One-way / Two way |
| -- |
| File: [activity_registration_user_info.xml](app/src/main/res/layout/activity_registration_user_info.xml)<br><br>Il _FrameLayout_ con id _sign_up_button_ binda le sue proprietà _background_ e _clickable_ alla proprietà dataValid del ViewModel<br><br>Il _TextInputEditText_ con id _firstname_ET_ binda la proprietà _text_ alla proprietà firstname del ViewModel. Utilizzando <kbd>@={model.firstname}</kbd> il binding viene fatto in Two-way quindi scrivendo del testo nella vista si aggiorna anche la proprietà del ViewModel e viceversa|  

| Converters |
| -- |
| Directory: [converters](app/src/main/java/it/italiancoders/mybudget/databinding/converters)<br><br>Un esempio è possibile trovarlo in [list_item_movement.xml](app/src/main/res/layout/list_item_movement.xml) in cui è stato aggiunto l'import nel tag data di [AmountStringConversion](app/src/main/java/it/italiancoders/mybudget/databinding/converters/AmountStringConversion.kt) e utilizzato per visualizzare il totale del documento.<br><br>Il converter è stato creato per visualizzare l'importo del documento formattato secondo le impostazioni di sistema con il simbolo della valuta impostato nelle settings dell'applicazione. |

| Adapters |
| -- |
| Directory: [adapters](app/src/main/java/it/italiancoders/mybudget/databinding/adapters)<br><br>Nel layout [activity_registration_user_info.xml](app/src/main/res/layout/activity_registration_user_info.xml) viene utilizzato l'adapter [ValidationBindingAdapter](app/src/main/java/it/italiancoders/mybudget/databinding/adapters/ValidationBindingAdapter.kt) su tutti i _TextInputLayout_.<br><br>Le proprietà che vengono prese in considerazione sono <kbd>app:validation</kbd> che rappresenta la rule da applicare per la validazione e <kbd>app:errorMsg</kbd> che rappresenta il messaggio di errore che verrà impostato se la rule non viene rispettata. |

## :heavy_check_mark: ViewModel

| ViewModel |
| -- |
| I ViewModel presenti sono utilizzati in activity, fragment e custom view all'interno di tutta l'applicazione. Di seguito viene riportato un esempio per ogni tipologia.<br><br>Activity: [RegistrationUserInfoActivity](app/src/main/java/it/italiancoders/mybudget/activity/registration/RegistrationUserInfoActivity.kt)<br><br>Fragment: [ListMovementsFragment](app/src/main/java/it/italiancoders/mybudget/activity/movements/list/ListMovementsFragment.kt)<br><br>Custom view: [SearchMovementsView](app/src/main/java/it/italiancoders/mybudget/activity/movements/search/SearchMovementsView.kt) |

| ViewModelFactory |
| -- |
| In Android non esiste la possibilità di poter instanziare un ViewModel con costruttore con 1 o più parametri attraverso un _ViewModelProviders_. Per questo motivo si rende necessario l'uso di _ViewModelFactory_<br><br>Nell'applicazione possiamo trovare l'uso di factory quasi ovunque. Un esempio può essere quello in [CategoriesActivity](app/src/main/java/it/italiancoders/mybudget/activity/categories/CategoriesActivity.kt) dove il ViewModel viene creato nel metodo _onCreate_ in questo modo<br><br>`ViewModelProvider(this,CategoriesViewModelFactory(categoriesManager))`<br>`.get(CategoriesViewModel::class.java)` |

##### LiveData

##### Observable

##### Animation
- ValueAnimator

##### Kotlin coroutine

##### Dagger injection
- Application
- Module
- Components

##### REST client
- Client
- Interceptor

##### Tutorial

##### RecyclerView
- PagerSnapHelper
- Adapter
- Item click listener

##### State Machine Pattern
- PeriodType

##### Activity startActivityForResult

##### BottomSheetBehavior

##### Firebase

##### Crashlytics

##### Settings
- Summary
- Custom preference
- Custom file

##### Splash screen

##### Room ORM
- Database
- Entity
- Dao
- Converter

##### Menu
- Global
- Activity

##### Network state monitor

##### Local Junit Test

##### Instrumented Test

##### Shared test
