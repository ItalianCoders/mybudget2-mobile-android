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
- [Animation](#heavy_check_mark-animation)
- [Kotlin](#heavy_check_mark-kotlin)
- [Dagger injection](#dagger-injection)
- [REST client](#rest-client)
- [Tutorial](#tutorial)
- [RecyclerView](#recyclerview)
- [State Machine](#heavy_check_mark-state-machine)
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

| One-way / Two-way |
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
:construction: In costruzione :construction:

##### Observable
:construction: In costruzione :construction:

## :heavy_check_mark: Animation

| ValueAnimator |
| -- |
| Esempio: [LoginActivity](app/src/main/java/it/italiancoders/mybudget/activity/login/LoginActivity.kt)<br><br>L'animaizoni utilizzatate nell'activity di login sono 2 e, al click del pulsante di login si occupano rispettivamente di:<br>- Ridurre la larghezza del pulsante<br>- Cambiare la trasparenza del testo del pulsante e alla fine visualizzare la progress bar |
<table width="100%">
  <tbody>
    <tr>
      <td>
        Prendendo come esempio la prima animazione ecco il codice:
        <pre>
          <code CLASS=Java>
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
          </code>
        </pre>
        Il ValueAnimator si basa su un range di valori dalla normale larghezza del pulsante a quella finale (finalWidth) definita nelle resources. Il listener aggiunto contiene il valore corrente dell'animazione (it.animatedValue) che viene utilizzato per settare la nuova larghezza al layout del pulsante (layoutParam.width).
      </td>
    </tr>
  </tbody>
</table>

<table width="100%">
  <tbody>
    <tr>
      <td>
      Per altri esempi sull'uso di animazioni con ValueAnimator o utilizzando un ConstraintLayout è possibile consultare i miei articoli a questi indirizzi:<br><br>
      <a href="https://italiancoders.it/android-creare-favolose-animazioni-con-constraintlayout-parte-1">Android: creare favolose animazioni con ConstraintLayout – Parte 1</a><br>
      <a href="https://italiancoders.it/android-creare-favolose-animazioni-con-constraintlayout-parte-2">Android: creare favolose animazioni con ConstraintLayout – Parte 2</a><br>
      </td>
    </tr>
  </tbody>
</table>

## :heavy_check_mark: Kotlin

| Extensions |
| -- |
| Per approfondire: [documentazione kotlinlang](https://kotlinlang.org/docs/reference/extensions.html)<br><br>Kotlin permette di aggiungere nuove funzionalità ad una classe senza doverla estendere tramite delle speciali dichiarazioni chiamate appunto _Kotlin extensions_.<br><br>Un esempio è possibile trovarlo nella classe [ViewModelExtensions](app/src/main/java/it/italiancoders/mybudget/utils/ViewModelExtensions.kt) in cui vengono aggiunti 3 nuovi metodi alla classe _ViewModel_: <kbd>uiJob</kbd>, <kbd>ioJob</kbd> e <kbd>backgroundJob</kbd>.<br><br>Nella classe [LoginViewModel](app/src/main/java/it/italiancoders/mybudget/activity/login/LoginViewModel.kt) nel metodo _login_ troviamo l'uso dei primi 2 metodi citati in quanto chiunque estenda _ViewModel_ potrà averli a disposizione.<br><br>Prendendo come riferimento Java tutto ciò sarebbe stato possibile realizzarlo creando una classe (astratta) _BaseViewModel_ che estende _ViewModel_ in cui implementare i 3 metodi e successivamente far estendere da questa classe _LoginViewModel_. |

| Coroutines |
| -- |
| Per approfondire: [documentazione kotlinlang](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)<br><br>Il capitolo delle coroutines in kotlin è molto vasto e prima di utilizzarle è assolutamente obbligatorio leggerne la documentazione. L'uso in questa applicazione per ora si limita alla gestione dei thread come quando succedeva in Java con l'utilizzo degli _AsyncTask_.<br><br>Nella classe [ViewModelExtensions](app/src/main/java/it/italiancoders/mybudget/utils/ViewModelExtensions.kt) vista prima ognuno dei metodi creati nella sua implementazione lancia un coroutine (<kbd>launch{}</kbd>) associata ad uno scope (ui,io o background).<br><br> |

##### Dagger injection
:construction: In costruzione :construction:
- Application
- Module
- Components

##### REST client
:construction: In costruzione :construction:
- Client
- Interceptor

##### Tutorial

:construction: In costruzione :construction:

##### RecyclerView
:construction: In costruzione :construction:
- PagerSnapHelper
- Adapter
- Item click listener

## :heavy_check_mark: State Machine

|<span style="font-weight:normal">L'uso di una _State Machine_ in Informatica rientra in uno dei pattern più usati tradizionalmente. Non si tratta di un pattern _coding oriented_, quelli per internderci che fanno parte dei tipi della Gang of Four, ma _system oriented_.<br><br> In questa applicazione possiamo trovare un esempio nella classe [PeriodType](app/src/main/java/it/italiancoders/mybudget/activity/main/PeriodType.kt). Il metodo <kbd>nextType()</kbd> resituisce lo stato successivo a cui quello attuale può portare.<br><br>Nel caso specifico dell'applicazione la successione degli stati è infinita in questo modo:<br>MONTH --> WEEK --> DAY --> MONTH --> WEEK --> ecc...</span>|
|:--|

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

##### Splash screen
:construction: In costruzione :construction:

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

##### Network state monitor
:construction: In costruzione :construction:

##### Local Junit Test
:construction: In costruzione :construction:

##### Instrumented Test
:construction: In costruzione :construction:

##### Shared test
:construction: In costruzione :construction:
