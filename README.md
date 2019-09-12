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

- [DataBinding](#databinding)
- [ViewModel](#viewmodel)
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

##### DataBinding

| One-way / Two way |
| -- |
| File: [activity_registration_user_info.xml](app/src/main/res/layout/activity_registration_user_info.xml)<br><br>Il _FrameLayout_ con id _sign_up_button_ binda le sue proprietà _background_ e _clickable_ alla proprietà dataValid del ViewModel<br><br>Il _TextInputEditText_ con id _firstname_ET_ binda la proprietà _text_ alla proprietà firstname del ViewModel. Utilizzando <kbd>@={model.firstname}</kbd> il binding viene fatto in Two-way quindi scrivendo del testo nella vista si aggiorna anche la proprietà del ViewModel e viceversa|  

| Converters |
| -- |
| Directory: [converters](app/src/main/java/it/italiancoders/mybudget/databinding/converters) |

| Adapters |
| -- |
| Directory: [adapters](app/src/main/java/it/italiancoders/mybudget/databinding/adapters) |

##### ViewModel
- ViewModel
- ViewModelFactory

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
