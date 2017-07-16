module.exports = function(grunt) {
    let orderItemPath = 'components';
    let orderPath = 'pages';
    let bootswatchPath = 'bower_components/bootswatch/journal/'
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        sass: {
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
                    src: ['bootstrap.min.css'],
                    dest: bootswatchPath,
                    ext: '.min.css'
                }]
            }
        },
        watch: {
            scripts: {
                files: [orderItemPath + '/**/*.scss', orderPath + '/**/*.scss', bootswatchPath + '*.css'],
                tasks: ['sass', 'cssmin']
            },
            livereload: {
                options: {
                    livereload: '<%=connect.options.livereload %>'
                },
                files: [
                    orderItemPath + '/**/*.css',
                    orderPath + '/**/*.css',
                    bootswatchPath + '*.min.css'
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


    grunt.registerTask('outputcss', ['sass']);
    grunt.registerTask('mincss', ['cssmin']);
    grunt.registerTask('watchit', ['outputcss', 'cssmin', 'connect', 'watch']);

    grunt.registerTask('default');
};