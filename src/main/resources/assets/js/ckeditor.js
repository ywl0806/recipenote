import ClassicEditor from '@ckeditor/ckeditor5-build-classic'
const readOnly = document.getElementById("readOnly");

if (readOnly === null) {
    ClassicEditor
        .create(document.querySelector('#editor'), {
            language: 'ja',
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
} else {
    ClassicEditor
        .create(document.querySelector('#description'))
        .then(editor => {

            window.editor = editor;
            const toolbarElement = editor.ui.view.toolbar.element;
            toolbarElement.style.display = 'none';
            editor.isReadOnly = true;
        })
        .catch(error => {
            console.error('There was a problem initializing the editor.', error);
        });

}



