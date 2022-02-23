// java script entry point

// BootstrapとjQueryのインポート
require('./bootstrap');

// Vueのインポート
import Vue from 'vue';
import StarRating from 'vue-star-rating'
Vue.component('star-rating', StarRating);

const app = new Vue({
  el: '#star',
    data: {
      rating: 0
    }
})

// Fontawesomeのインポート
import {config, dom, library} from '@fortawesome/fontawesome-svg-core';
import {faBlender,faLock,faLockOpen} from '@fortawesome/free-solid-svg-icons';
import {faComments} from '@fortawesome/free-regular-svg-icons';

library.add(faComments,faBlender,faLock,faLockOpen);

dom.i2svg();
dom.watch();
