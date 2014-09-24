describe('GimmickParser', function() {

/*
    testGimmick.addHandler('multiline', {
        namepsace: 'g',
        trigger: 'somegimmick',
        callback: myCallback
    });
*/
    beforeEach(function() {
        loadFixtures('gimmick.html');
    });

    // finding checks
    it('should not treat a singleline gimmick as a multiline gimmick', function() {
        var $fixture = $('#multiline-gimmick, #singleline-gimmick-simpleoptions');
        var parser = new MDwiki.Gimmick.GimmickParser($fixture);
        parser.parse();
        expect(parser.multilineGimmicks.length).toBe(1);
        expect(parser.singlelineGimmicks.length).toBe(1);
    });

    describe('multiline gimmicks', function() {
       // MULTILINE GIMMICKS
        it('should find a mulitline gimmick', function() {
            var $mlg = $('#multiline-gimmick');
            var parser = new MDwiki.Gimmick.GimmickParser($mlg);
            parser.parse();
            expect(parser.multilineGimmicks.length).toBe(1);
        });
        it('should find the info text of a multiline gimmick', function() {
            var $mlg = $('#multiline-gimmick');
            var parser = new MDwiki.Gimmick.GimmickParser($mlg);
            parser.parse();
            var first_match = parser.multilineGimmicks[0];
            expect(first_match.trigger).toBe('somegimmick');
            expect(first_match.text).toBe('This is a text');
        });
        it('should find the info text of a mulitline gimmick', function() {
            var $mlg = $('#multiline-gimmick');
            var parser = new MDwiki.Gimmick.GimmickParser($mlg);
            parser.parse();
            var first_match = parser.multilineGimmicks[0];
            expect(first_match.trigger).toBe('somegimmick');
            expect(first_match.text).toBe('This is a text');
        });
    });

    describe('singleline gimmicks', function() {
        it('should find a singleline gimmick', function() {
            var $slg = $('#singleline-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($slg);
            parser.parse();
            expect(parser.singlelineGimmicks.length).toBe(1);
        });

        it('should find the trigger of a singleline gimmick', function() {
            var $slg= $('#singleline-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($slg);
            parser.parse();
            var first_match = parser.singlelineGimmicks[0];
            expect(first_match.trigger).toBe('somegimmick');
        });

        it('should find the options of a singleline gimmick', function() {
            var $slg= $('#singleline-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($slg);
            parser.parse();
            var first_match = parser.singlelineGimmicks[0];
            expect(first_match.options.param1).toBe('foo');
        });

        // TODO complex options tests
        it('should find a null options for a singleline gimmick without options', function() {
            var $slg= $('#singleline-gimmick-nooptions');
            var parser = new MDwiki.Gimmick.GimmickParser($slg);
            parser.parse();
            var first_match = parser.singlelineGimmicks[0];
            expect(first_match.options).toBeNull();
        });
    });
    describe('link gimmick', function() {
        it('should find a link gimmick', function() {
            var $lg = $('#link-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            expect(parser.linkGimmicks.length).toBe(1);
        });
        it('should be able to retrieve the trigger of a link gimmick', function() {
            var $lg = $('#link-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            var first_match = parser.linkGimmicks[0];
            expect(first_match.trigger).toBe('somegimmick');
        });
        it('should be able to retrieve the href-text of a link gimmick', function() {
            var $lg = $('#link-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            var first_match = parser.linkGimmicks[0];
            expect(first_match.text).toBe('This is a text.');
        });
        it('should be able to retrieve the options of a link gimmick', function() {
            var $lg = $('#link-gimmick-simpleoptions');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            var first_match = parser.linkGimmicks[0];
            expect(first_match.options.param1).toBe('foo');
        });
        it('should set the options to null if no options are specified', function() {
            var $lg = $('#link-gimmick-nooptions');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            var first_match = parser.linkGimmicks[0];
            expect(first_match.options).toBeNull();
        });
        it('should not treat a regular link as link gimmick', function() {
            var $lg = $('#link-gimmick-with-regular-link');
            var parser = new MDwiki.Gimmick.GimmickParser($lg);
            parser.parse();
            expect(parser.linkGimmicks.length).toBe(1);
            expect(parser.linkGimmicks[0].text).toBe('This is a text.');
        });
    });
});
