package com.example.data.bible

import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.CrossReference
import com.example.data.model.Translation
import com.example.data.model.WordStudyEntry

object BibleRepository {

    val books: List<BibleBook> = listOf(
        BibleBook("Genesis", "Old Testament", 50),
        BibleBook("Psalms", "Old Testament", 150),
        BibleBook("John", "New Testament", 21),
        BibleBook("Romans", "New Testament", 16),
        BibleBook("Ephesians", "New Testament", 6),
        BibleBook("Philippians", "New Testament", 4),
        BibleBook("James", "New Testament", 5)
    )

    // Strong's Dictionary for Word Study Mode
    val wordStudyDictionary: Map<String, WordStudyEntry> = mapOf(
        "grace" to WordStudyEntry(
            englishWord = "grace",
            originalWord = "χάρις",
            language = "Greek",
            transliteration = "charis",
            strongsNumber = "G5485",
            pronunciation = "khar'-ece",
            definition = "Unmerited divine favor, goodwill, benevolence, and lovingkindness freely bestowed upon the underserving.",
            theologicalSignificance = "The foundation of Christian soteriology. Grace is not merely God overlooking sin, but God actively supplying through Christ what He righteously demands from humanity."
        ),
        "love" to WordStudyEntry(
            englishWord = "love",
            originalWord = "ἀγάπη",
            language = "Greek",
            transliteration = "agapē",
            strongsNumber = "G26",
            pronunciation = "ag-ah'-pay",
            definition = "Self-sacrificing, unconditional, deliberate love that seeks the highest good of the beloved regardless of cost or reciprocity.",
            theologicalSignificance = "The supreme nature of God's character (1 John 4:8) and the quintessential mark of Christian discipleship demonstrated at the cross (Romans 5:8)."
        ),
        "word" to WordStudyEntry(
            englishWord = "word",
            originalWord = "λόγος",
            language = "Greek",
            transliteration = "logos",
            strongsNumber = "G3056",
            pronunciation = "log'-os",
            definition = "A vocal utterance, divine decree, revelation, or supreme reason and mind of God embodied.",
            theologicalSignificance = "In John 1:1, Logos transcends philosophical abstractions to declare the eternal second person of the Trinity who took on human flesh to reveal God's glory."
        ),
        "faith" to WordStudyEntry(
            englishWord = "faith",
            originalWord = "πίστις",
            language = "Greek",
            transliteration = "pistis",
            strongsNumber = "G4102",
            pronunciation = "pis'-tis",
            definition = "Firm conviction, unwavering trust, reliance, and allegiance to God and His promises.",
            theologicalSignificance = "The instrumental means by which sinners receive justification (Romans 3:28). Faith is not blind optimism or a good work, but an empty hand receiving God's gift."
        ),
        "righteousness" to WordStudyEntry(
            englishWord = "righteousness",
            originalWord = "δικαιοσύνη",
            language = "Greek",
            transliteration = "dikaiosynē",
            strongsNumber = "G1343",
            pronunciation = "dik-ah-yos-oo'-nay",
            definition = "Conformity to the divine character and standard; the state of approval and equity before God.",
            theologicalSignificance = "God's own moral perfection which is imputed (credited) to the believer's account through faith in Jesus Christ, establishing legal standing before the divine bar."
        ),
        "spirit" to WordStudyEntry(
            englishWord = "spirit",
            originalWord = "πνεῦμα",
            language = "Greek",
            transliteration = "pneuma",
            strongsNumber = "G4151",
            pronunciation = "pnyoo'-mah",
            definition = "Breath, wind, the vital life principle, the non-physical essence of man, or the Holy Spirit.",
            theologicalSignificance = "Jesus compares the regenerating activity of the Spirit to the mysterious wind (John 3:8). The Spirit indwells, transforms, seals, and guides believers into all truth."
        ),
        "peace" to WordStudyEntry(
            englishWord = "peace",
            originalWord = "εἰρήνη",
            language = "Greek",
            transliteration = "eirēnē",
            strongsNumber = "G1515",
            pronunciation = "i-ray'-nay",
            definition = "Harmonious relationship, tranquility of soul, absence of hostility, spiritual wholeness.",
            theologicalSignificance = "Carries the rich Hebrew concept of 'shalom' — not merely the absence of conflict, but full objective reconciliation with God through Christ's blood."
        ),
        "beginning" to WordStudyEntry(
            englishWord = "beginning",
            originalWord = "בְּרֵאשִׁית",
            language = "Hebrew",
            transliteration = "bereshit",
            strongsNumber = "H7225",
            pronunciation = "ber-ay-sheeth'",
            definition = "The first origin, inception, commencement of time and ordered creation.",
            theologicalSignificance = "Affirms that time and physical cosmos had an absolute inception engineered ex nihilo (out of nothing) by the transcendent God."
        ),
        "good" to WordStudyEntry(
            englishWord = "good",
            originalWord = "ἀγαθός",
            language = "Greek",
            transliteration = "agathos",
            strongsNumber = "G18",
            pronunciation = "ag-ath-os'",
            definition = "Inherently excellent, morally upright, beneficial, delivering lasting spiritual blessing.",
            theologicalSignificance = "In Romans 8:28, the 'good' is conformity to the image of Jesus Christ rather than temporal comfort or earthly ease."
        ),
        "fellowship" to WordStudyEntry(
            englishWord = "fellowship",
            originalWord = "κοινωνία",
            language = "Greek",
            transliteration = "koinōnia",
            strongsNumber = "G2842",
            pronunciation = "koy-nohn-ee'-ah",
            definition = "Deep communion, joint participation, shared life, and mutual sacrificial partnership.",
            theologicalSignificance = "Describes the horizontal union of Christians rooted in their vertical union with the Father and the Son (1 John 1:3; Acts 2:42)."
        ),
        "called" to WordStudyEntry(
            englishWord = "called",
            originalWord = "κλητός",
            language = "Greek",
            transliteration = "klētos",
            strongsNumber = "G2822",
            pronunciation = "klay-tos'",
            definition = "Divinely summoned, invited, appointed by God to salvation and kingdom service.",
            theologicalSignificance = "In Paul's epistles, this often refers to the efficacious, inward summons of the Holy Spirit that unfailingly awakens saving faith."
        )
    )

    // Parallel Verses Database across BSB, WEB, KJV
    private val rawVerses = listOf(
        // Romans 8
        Triple("Romans", 8, listOf(
            Triple(28,
                "And we know that God causes all things to work together for good to those who love God, to those who are called according to His purpose.",
                "We know that all things work together for good for those who love God, to those who are called according to his purpose." to "And we know that all things work together for good to them that love God, to them who are the called according to his purpose."),
            Triple(29,
                "For those whom He foreknew, He also predestined to become conformed to the image of His Son, so that He would be the firstborn among many brethren;",
                "For whom he foreknew, he also predestined to be conformed to the image of his Son, that he might be the firstborn among many brothers." to "For whom he did foreknow, he also did predestinate to be conformed to the image of his Son, that he might be the firstborn among many brethren."),
            Triple(30,
                "and these whom He predestined, He also called; and these whom He called, He also justified; and these whom He justified, He also glorified.",
                "Whom he predestined, those he also called. Whom he called, those he also justified. Whom he justified, those he also glorified." to "Moreover whom he did predestinate, them he also called: and whom he called, them he also justified: and whom he justified, them he also glorified."),
            Triple(31,
                "What then shall we say to these things? If God is for us, who is against us?",
                "What then shall we say about these things? If God is for us, who can be against us?" to "What shall we then say to these things? If God be for us, who can be against us?"),
            Triple(32,
                "He who did not spare His own Son, but delivered Him over for us all, how will He not also with Him freely give us all things?",
                "He who didn't spare his own Son, but delivered him up for us all, how would he not also with him freely give us all things?" to "He that spared not his own Son, but delivered him up for us all, how shall he not with him also freely give us all things?"),
            Triple(35,
                "Who will separate us from the love of Christ? Will tribulation, or distress, or persecution, or famine, or nakedness, or peril, or sword?",
                "Who shall separate us from the love of Christ? Could oppression, or anguish, or persecution, or famine, or nakedness, or peril, or sword?" to "Who shall separate us from the love of Christ? shall tribulation, or distress, or persecution, or famine, or nakedness, or peril, or sword?"),
            Triple(38,
                "For I am convinced that neither death, nor life, nor angels, nor principalities, nor things present, nor things to come, nor powers,",
                "For I am persuaded that neither death, nor life, nor angels, nor principalities, nor things present, nor things to come, nor powers," to "For I am persuaded, that neither death, nor life, nor angels, nor principalities, nor powers, nor things present, nor things to come,"),
            Triple(39,
                "nor height, nor depth, nor any other created thing, will be able to separate us from the love of God, which is in Christ Jesus our Lord.",
                "nor height, nor depth, nor any other created thing, will be able to separate us from God's love, which is in Christ Jesus our Lord." to "Nor height, nor depth, nor any other creature, shall be able to separate us from the love of God, which is in Christ Jesus our Lord.")
        )),

        // Romans 1
        Triple("Romans", 1, listOf(
            Triple(16,
                "For I am not ashamed of the gospel, for it is the power of God for salvation to everyone who believes, to the Jew first and also to the Greek.",
                "For I am not ashamed of the Good News of Christ, because it is the power of God for salvation for everyone who believes, for the Jew first, and also for the Greek." to "For I am not ashamed of the gospel of Christ: for it is the power of God unto salvation to every one that believeth; to the Jew first, and also to the Greek."),
            Triple(17,
                "For in it the righteousness of God is revealed from faith to faith; as it is written, 'But the righteous man shall live by faith.'",
                "For in it is revealed God's righteousness from faith to faith. As it is written, 'But the righteous shall live by faith.'" to "For therein is the righteousness of God revealed from faith to faith: as it is written, The just shall live by faith.")
        )),

        // Romans 3
        Triple("Romans", 3, listOf(
            Triple(21,
                "But now apart from the Law the righteousness of God has been manifested, being witnessed by the Law and the Prophets,",
                "But now apart from the law, a righteousness of God has been revealed, being testified by the law and the prophets;" to "But now the righteousness of God without the law is manifested, being witnessed by the law and the prophets;"),
            Triple(22,
                "even the righteousness of God through faith in Jesus Christ for all those who believe; for there is no distinction;",
                "even the righteousness of God through faith in Jesus Christ to all and on all those who believe. For there is no distinction," to "Even the righteousness of God which is by faith of Jesus Christ unto all and upon all them that believe: for there is no difference:"),
            Triple(23,
                "for all have sinned and fall short of the glory of God,",
                "for all have sinned, and fall short of the glory of God;" to "For all have sinned, and come short of the glory of God;"),
            Triple(24,
                "being justified as a gift by His grace through the redemption which is in Christ Jesus;",
                "being justified freely by his grace through the redemption that is in Christ Jesus;" to "Being justified freely by his grace through the redemption that is in Christ Jesus;"),
            Triple(25,
                "whom God displayed publicly as a propitiation in His blood through faith. This was to demonstrate His righteousness, because in the forbearance of God He passed over the sins previously committed;",
                "whom God sent to be an atoning sacrifice, through faith in his blood, for a demonstration of his righteousness through the passing over of prior sins, in God's forbearance;" to "Whom God hath set forth to be a propitiation through faith in his blood, to declare his righteousness for the remission of sins that are past, through the forbearance of God;")
        )),

        // Romans 5
        Triple("Romans", 5, listOf(
            Triple(1,
                "Therefore, having been justified by faith, we have peace with God through our Lord Jesus Christ,",
                "Being therefore justified by faith, we have peace with God through our Lord Jesus Christ;" to "Therefore being justified by faith, we have peace with God through our Lord Jesus Christ:"),
            Triple(2,
                "through whom also we have obtained our introduction by faith into this grace in which we stand; and we exult in hope of the glory of God.",
                "through whom we also have our access by faith into this grace in which we stand. We rejoice in hope of the glory of God." to "By whom also we have access by faith into this grace wherein we stand, and rejoice in hope of the glory of God."),
            Triple(8,
                "But God demonstrates His own love toward us, in that while we were yet sinners, Christ died for us.",
                "But God commends his own love toward us, in that while we were yet sinners, Christ died for us." to "But God commendeth his love toward us, in that, while we were yet sinners, Christ died for us.")
        )),

        // John 1
        Triple("John", 1, listOf(
            Triple(1,
                "In the beginning was the Word, and the Word was with God, and the Word was God.",
                "In the beginning was the Word, and the Word was with God, and the Word was God." to "In the beginning was the Word, and the Word was with God, and the Word was God."),
            Triple(2,
                "He was in the beginning with God.",
                "The same was in the beginning with God." to "The same was in the beginning with God."),
            Triple(3,
                "All things came into being through Him, and apart from Him nothing came into being that has come into being.",
                "All things were made through him. Without him was not anything made that has been made." to "All things were made by him; and without him was not any thing made that was made."),
            Triple(4,
                "In Him was life, and the life was the Light of men.",
                "In him was life, and the life was the light of men." to "In him was life; and the life was the light of men."),
            Triple(14,
                "And the Word became flesh, and dwelt among us, and we saw His glory, glory as of the only begotten from the Father, full of grace and truth.",
                "The Word became flesh, and lived among us. We saw his glory, such glory as of the one and only Son of the Father, full of grace and truth." to "And the Word was made flesh, and dwelt among us, (and we beheld his glory, the glory as of the only begotten of the Father,) full of grace and truth.")
        )),

        // John 3
        Triple("John", 3, listOf(
            Triple(3,
                "Jesus answered and said to him, 'Truly, truly, I say to you, unless one is born again he cannot see the kingdom of God.'",
                "Jesus answered him, 'Most certainly, I tell you, unless one is born anew, he can't see God's Kingdom.'" to "Jesus answered and said unto him, Verily, verily, I say unto thee, Except a man be born again, he cannot see the kingdom of God."),
            Triple(16,
                "For God so loved the world, that He gave His only begotten Son, that whoever believes in Him shall not perish, but have eternal life.",
                "For God so loved the world, that he gave his one and only Son, that whoever believes in him should not perish, but have eternal life." to "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.")
        )),

        // Ephesians 2
        Triple("Ephesians", 2, listOf(
            Triple(8,
                "For by grace you have been saved through faith; and that not of yourselves, it is the gift of God;",
                "For by grace you have been saved through faith, and that not of yourselves; it is the gift of God," to "For by grace are ye saved through faith; and that not of yourselves: it is the gift of God:"),
            Triple(9,
                "not as a result of works, so that no one may boast.",
                "not of works, that no one would boast." to "Not of works, lest any man should boast."),
            Triple(10,
                "For we are His workmanship, created in Christ Jesus for good works, which God prepared beforehand so that we would walk in them.",
                "For we are his workmanship, created in Christ Jesus for good works, which God prepared before that we would walk in them." to "For we are his workmanship, created in Christ Jesus unto good works, which God hath before ordained that we should walk in them.")
        )),

        // Genesis 1
        Triple("Genesis", 1, listOf(
            Triple(1,
                "In the beginning God created the heavens and the earth.",
                "In the beginning, God created the heavens and the earth." to "In the beginning God created the heaven and the earth."),
            Triple(2,
                "The earth was formless and void, and darkness was over the surface of the deep, and the Spirit of God was moving over the surface of the waters.",
                "The earth was formless and empty. Darkness was on the surface of the deep and God's Spirit was hovering over the surface of the waters." to "And the earth was without form, and void; and darkness was upon the face of the deep. And the Spirit of God moved upon the face of the waters."),
            Triple(3,
                "Then God said, 'Let there be light'; and there was light.",
                "God said, 'Let there be light,' and there was light." to "And God said, Let there be light: and there was light.")
        )),

        // Psalms 23
        Triple("Psalms", 23, listOf(
            Triple(1,
                "The LORD is my shepherd, I shall not want.",
                "Yahweh is my shepherd: I shall lack nothing." to "The LORD is my shepherd; I shall not want."),
            Triple(2,
                "He makes me lie down in green pastures; He leads me beside quiet waters.",
                "He makes me lie down in green pastures. He leads me beside still waters." to "He maketh me to lie down in green pastures: he leadeth me beside the still waters."),
            Triple(3,
                "He restores my soul; He guides me in the paths of righteousness for His name's sake.",
                "He restores my soul. He guides me in the paths of righteousness for his name's sake." to "He restoreth my soul: he leadeth me in the paths of righteousness for his name's sake."),
            Triple(6,
                "Surely goodness and lovingkindness will follow me all the days of my life, and I will dwell in the house of the LORD forever.",
                "Surely goodness and loving kindness shall follow me all the days of my life, and I will dwell in Yahweh's house forever." to "Surely goodness and mercy shall follow me all the days of my life: and I will dwell in the house of the LORD for ever.")
        )),

        // Philippians 4
        Triple("Philippians", 4, listOf(
            Triple(4,
                "Rejoice in the Lord always; again I will say, rejoice!",
                "Rejoice in the Lord always! Again I will say, 'Rejoice!'" to "Rejoice in the Lord alway: and again I say, Rejoice."),
            Triple(6,
                "Be anxious for nothing, but in everything by prayer and supplication with thanksgiving let your requests be made known to God.",
                "In nothing be anxious, but in everything, by prayer and petition with thanksgiving, let your requests be made known to God." to "Be careful for nothing; but in every thing by prayer and supplication with thanksgiving let your requests be made known unto God."),
            Triple(7,
                "And the peace of God, which surpasses all comprehension, will guard your hearts and your minds in Christ Jesus.",
                "And the peace of God, which surpasses all understanding, will guard your hearts and your thoughts in Christ Jesus." to "And the peace of God, which passeth all understanding, shall keep your hearts and minds through Christ Jesus.")
        ))
    )

    fun getVerses(book: String, chapter: Int, translation: Translation): List<BibleVerse> {
        val bookChapter = rawVerses.find { it.first.equals(book, ignoreCase = true) && it.second == chapter }
            ?: return emptyList()

        return bookChapter.third.map { (vNum, bsbText, parallel) ->
            val text = when (translation) {
                Translation.BSB -> bsbText
                Translation.WEB -> parallel.first
                Translation.KJV -> parallel.second
            }
            BibleVerse(book, chapter, vNum, text, translation)
        }
    }

    fun getParallelTranslations(book: String, chapter: Int, verseNum: Int): Map<Translation, String> {
        val bookChapter = rawVerses.find { it.first.equals(book, ignoreCase = true) && it.second == chapter }
            ?: return emptyMap()

        val item = bookChapter.third.find { it.first == verseNum } ?: return emptyMap()

        return mapOf(
            Translation.BSB to item.second,
            Translation.WEB to item.third.first,
            Translation.KJV to item.third.second
        )
    }

    // Cross-references dataset
    val crossReferences: Map<String, List<CrossReference>> = mapOf(
        "Romans 8:28" to listOf(
            CrossReference("Romans 8:28", "Genesis 50:20", "You intended to harm me, but God intended it for good to accomplish what is now being done..."),
            CrossReference("Romans 8:28", "Ephesians 1:11", "In Him we were also chosen, having been predestined according to the plan of Him who works out everything in conformity with the purpose of His will..."),
            CrossReference("Romans 8:28", "2 Corinthians 4:17", "For our light and momentary troubles are achieving for us an eternal glory that far outweighs them all.")
        ),
        "John 1:1" to listOf(
            CrossReference("John 1:1", "Genesis 1:1", "In the beginning God created the heavens and the earth."),
            CrossReference("John 1:1", "1 John 1:1", "That which was from the beginning, which we have heard, which we have seen with our eyes..."),
            CrossReference("John 1:1", "Colossians 1:16-17", "For in Him all things were created: things in heaven and on earth, visible and invisible..."),
            CrossReference("John 1:1", "Revelation 19:13", "He is dressed in a robe dipped in blood, and His name is the Word of God.")
        ),
        "Romans 3:23" to listOf(
            CrossReference("Romans 3:23", "Romans 3:10", "As it is written: 'There is no one righteous, not even one;'"),
            CrossReference("Romans 3:23", "Ecclesiastes 7:20", "Indeed, there is no one on earth who is righteous, no one who does what is right and never sins."),
            CrossReference("Romans 3:23", "Isaiah 53:6", "We all, like sheep, have gone astray, each of us has turned to our own way...")
        ),
        "Ephesians 2:8" to listOf(
            CrossReference("Ephesians 2:8", "Romans 3:24", "Being justified freely by His grace through the redemption that is in Christ Jesus."),
            CrossReference("Ephesians 2:8", "Titus 3:5", "He saved us, not because of righteous things we had done, but because of His mercy."),
            CrossReference("Ephesians 2:8", "Galatians 2:16", "Know that a person is not justified by the works of the law, but by faith in Jesus Christ.")
        ),
        "Philippians 4:6" to listOf(
            CrossReference("Philippians 4:6", "1 Peter 5:7", "Cast all your anxiety on Him because He cares for you."),
            CrossReference("Philippians 4:6", "Matthew 6:25", "Therefore I tell you, do not worry about your life, what you will eat or drink; or about your body..."),
            CrossReference("Philippians 4:6", "Psalm 55:22", "Cast your cares on the LORD and He will sustain you; He will never let the righteous be shaken.")
        ),
        "Genesis 1:1" to listOf(
            CrossReference("Genesis 1:1", "Psalm 102:25", "In the beginning you laid the foundations of the earth, and the heavens are the work of your hands."),
            CrossReference("Genesis 1:1", "Hebrews 11:3", "By faith we understand that the universe was formed at God's command, so that what is seen was not made out of what was visible.")
        )
    )

    fun getCrossReferencesForVerse(ref: String): List<CrossReference> {
        return crossReferences[ref] ?: crossReferences.entries.firstOrNull { ref.startsWith(it.key) }?.value ?: emptyList()
    }
}
