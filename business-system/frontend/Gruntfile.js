module.exports = function(grunt) {
    let component = orderItemPath = 'components';
    let page = orderPath = 'pages';
    let bower = 'bower_components/';
    let lib = 'static/lib/';
    let bootswatchPath = 'bower_components/bootswatch/journal/'
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        sass: {
            options: {
                sourcemap: 'none'
            },
            output: {
                files: [{
                        expand: true,
                        cwd: orderItemPath,
                        src: '**/*.scss',
                        dest: orderItemPath,
                        ext: '.css'
                    },
                    {
                        expand: true,
                        cwd: orderPath,
                        src: '**/*.scss',
                        dest: orderPath,
                        ext: '.css'
                    }
                ]
            }
        },
        jshint: {
            options: {
                esversion: 6
            },
            all: ['Gruntfule.js', 'index.js', 'components/**/*.js', 'pages/**/*.js']
        },
        concat: {
            options: {
                separator: ';',
            },
            basic_and_extras: {
                files: {
                    'pages/user/asset/user.js': [
                        page + '/user/user.js',
                        component + '/bind/bind.js',
                        component + '/out/out.js',
                        component + '/in/in.js'
                    ],
                    'pages/good/asset/good.js': [
                        page + '/good/good.js',
                        component + '/goodItem/goodItem.js',
                        component + '/goodSearch/goodSearch.js'
                    ],


                }
            }
        },
        cssmin: {
            target: {
                files: [{
                    expand: true,
                    cwd: bootswatchPath,
                    src: ['bootstrap.css'],
                    dest: bootswatchPath,
                    ext: '.min.css'
                }, {
                    expand: true,
                    cwd: page,
                    src: ['**/*.css'],
                    dest: page,
                    ext: '.min.css'
                }, {
                    expand: true,
                    cwd: component,
                    src: ['**/*.css'],
                    dest: component,
                    ext: '.min.css'
                }]
            }
        },
        concat_css: {
            files: { 'pages/goodUpload/goodUpload.all.css': ['pages/goodUpload/goodUpload.min.css', 'pages/upload/upload.min.css'] }
        },
        watch: {
            scripts: {
                files: [orderItemPath + '/**/*.scss',
                    orderPath + '/**/*.scss',
                    bootswatchPath + '*.css',
                    component + '/**/*.css',
                    page + '/**/*.css'
                ],
                tasks: ['sass', 'cssmin']
            },
            livereload: {
                options: {
                    livereload: '<%=connect.options.livereload %>'
                },
                files: [
                    orderItemPath + '/**/*.css',
                    orderPath + '/**/*.css',
                    bootswatchPath + '*.min.css',
                    component + '/**/*.min.css',
                    page + "/**/*.min.css"
                ]
            }
        },
        connect: {
            options: {
                port: 9001,
                open: true,
                livereload: 35729,
                hostname: 'localhost'
            },
            server: {
                options: {
                    port: 9001,
                    base: "./"
                }
            }
        }

    });
    grunt.loadNpmTasks('grunt-contrib-sass');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-connect');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-concat-css');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');



    grunt.registerTask('outputcss', ['sass']);
    grunt.registerTask('compresscss', ['cssmin']);
    grunt.registerTask('concatcss', ['concat_css']);
    grunt.registerTask('checkjs', ['jshint']);
    grunt.registerTask('concatjs', ['concat']);
    grunt.registerTask('watchit', ['outputcss', 'compresscss', 'connect', 'watch', 'concatcss']);

    grunt.registerTask('default');
};