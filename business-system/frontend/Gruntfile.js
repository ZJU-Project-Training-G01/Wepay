module.exports = function(grunt) {
    let component = orderItemPath = 'components';
    let page = orderPath = 'pages';

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


    grunt.registerTask('outputcss', ['sass']);
    grunt.registerTask('mincss', ['cssmin']);
    grunt.registerTask('watchit', ['outputcss', 'mincss', 'connect', 'watch']);

    grunt.registerTask('default');
};