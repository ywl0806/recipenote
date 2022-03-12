import ClassicEditor from '@ckeditor/ckeditor5-build-classic';


ClassicEditor
    .create(document.querySelector('#editor'), {
        toolbar: {
            items: [
                'heading',
                '|',
                'bold',
                'italic',
                'link',
                'bulletedList',
                'numberedList',
                '|',
                'outdent',
                'indent',
                '|',
                'imageUpload',
                'blockQuote',
                'insertTable',
                'mediaEmbed',
                'undo',
                'redo',
                'alignment',
                'fontColor',
                'fontBackgroundColor',
                'imageInsert',
                'fontSize',
                'highlight'
            ]
        },
        language: 'ja',
        image: {
            toolbar: [
                'imageTextAlternative',
                'imageStyle:inline',
                'imageStyle:block',
                'imageStyle:side',
                'linkImage'
            ]
        },
        table: {
            contentToolbar: [
                'tableColumn',
                'tableRow',
                'mergeTableCells',
                'tableCellProperties',
                'tableProperties'
            ]
        },
        simpleUpload: {
            uploadUrl: '/upload-image',
            withCredintials: true
        }
    })
    .then(editor => {
        window.editor = editor;
    })
    .catch(error => {
        console.error('There was a problem initializing the editor.', error);
    });
