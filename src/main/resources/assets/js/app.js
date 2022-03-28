// java script entry point

// BootstrapとjQueryのインポート
require('./bootstrap');


// Vueのインポート
import Vue from 'vue';
import StarRating from 'vue-star-rating'
Vue.component('star-rating', StarRating);


// Fontawesomeのインポート
import {config, dom, library} from '@fortawesome/fontawesome-svg-core';
import {fas,faRuler,faBlender,faLock,faLockOpen,faArrowCircleUp,faPlus,faSearch,faAngleLeft,faAngleRight,faStore,faUser} from '@fortawesome/free-solid-svg-icons';
import {faComments, far } from '@fortawesome/free-regular-svg-icons';
import {fab,faLine,faStackExchange} from '@fortawesome/free-brands-svg-icons'

library.add(faComments,faBlender,faLock,faLockOpen,faArrowCircleUp,faPlus
    ,faSearch,faAngleLeft,faAngleRight,faStore,faUser,fas,faRuler,far,faLine,fab,faStackExchange);


dom.i2svg();
dom.watch();
