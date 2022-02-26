// java script entry point

// BootstrapとjQueryのインポート
require('./bootstrap');


// Vueのインポート
import Vue from 'vue';
import StarRating from 'vue-star-rating'
Vue.component('star-rating', StarRating);


// Fontawesomeのインポート
import {config, dom, library} from '@fortawesome/fontawesome-svg-core';
import {faBlender,faLock,faLockOpen,faArrowCircleUp} from '@fortawesome/free-solid-svg-icons';
import {faComments} from '@fortawesome/free-regular-svg-icons';

library.add(faComments,faBlender,faLock,faLockOpen,faArrowCircleUp);


dom.i2svg();
dom.watch();
