package com.example.data.local

object SeedData {
    suspend fun populateDatabase(dao: AppDao) {
        // 1. Current User
        val defaultUser = UserEntity(
            id = "user_me",
            name = "Devoted Reader",
            email = "reader@growingdeep.org",
            role = "leader",
            currentGroupId = "group_grace_fellowship"
        )
        dao.upsertUser(defaultUser)

        // 2. Starter Group
        val starterGroup = GroupEntity(
            id = "group_grace_fellowship",
            name = "Grace Fellowship Small Group",
            leaderId = "user_me",
            joinCode = "DEEP-7749",
            description = "A weekly community committed to deepening our roots in the Word, prayer, and mutual discipleship."
        )
        dao.insertGroup(starterGroup)

        // 3. Group Members
        val members = listOf(
            GroupMemberEntity(groupId = "group_grace_fellowship", userId = "user_me", userName = "Devoted Reader (You)", userRole = "Leader"),
            GroupMemberEntity(groupId = "group_grace_fellowship", userId = "user_sarah", userName = "Sarah Jenkins", userRole = "Member"),
            GroupMemberEntity(groupId = "group_grace_fellowship", userId = "user_david", userName = "David Chen", userRole = "Member"),
            GroupMemberEntity(groupId = "group_grace_fellowship", userId = "user_marcus", userName = "Marcus Vance", userRole = "Member"),
            GroupMemberEntity(groupId = "group_grace_fellowship", userId = "user_elena", userName = "Elena Rodriguez", userRole = "Member")
        )
        members.forEach { dao.insertMember(it) }

        // 4. Starter Guide: Foundational (15 lessons)
        val foundationalGuide = GuideEntity(
            id = "guide_foundational",
            title = "Foundations of Faith",
            level = "Foundational",
            description = "A 15-lesson comprehensive journey through core Christian doctrine and spiritual formation.",
            authorId = "system",
            isPublic = true,
            totalLessons = 15
        )

        val intermediateGuide = GuideEntity(
            id = "guide_romans_deep",
            title = "Unshakeable Hope: Romans 8",
            level = "Intermediate",
            description = "An in-depth 6-part study through life in the Spirit, suffering, and eternal security in Romans 8.",
            authorId = "system",
            isPublic = true,
            totalLessons = 6
        )

        val advancedGuide = GuideEntity(
            id = "guide_covenant_theology",
            title = "The Biblical Covenants",
            level = "Advanced",
            description = "Tracing God's redemptive promise from Adam, Noah, Abraham, and Moses to the New Covenant in Christ.",
            authorId = "system",
            isPublic = true,
            totalLessons = 8
        )

        dao.insertGuides(listOf(foundationalGuide, intermediateGuide, advancedGuide))

        // 5. Lessons for Foundational Guide (15 Lessons)
        val lessons = listOf(
            LessonEntity(
                id = "lesson_foundational_1",
                guideId = "guide_foundational",
                orderIndex = 1,
                title = "The Gospel",
                objective = "Understand that the gospel is the good news of what God accomplished through the life, death, and resurrection of Jesus Christ.",
                keyVerses = listOf("Romans 1:16-17", "1 Corinthians 15:1-4", "Ephesians 2:8-9"),
                teachingPoints = listOf(
                    "The gospel is fundamentally good news to be received, not good advice to be performed.",
                    "Sin alienated humanity from the holy creator God, incurring righteous condemnation.",
                    "Christ died for our sins according to the Scriptures, was buried, and was raised on the third day.",
                    "Salvation is entirely by grace alone through faith alone in Christ alone, excluding human boasting."
                ),
                discussionQuestions = listOf(
                    "How does understanding the gospel as 'good news announced' differ from treating it as moralistic advice?",
                    "What part of the gospel message do you find easiest to forget during a demanding week?",
                    "How does Ephesians 2:8-9 dismantle spiritual pride or insecurity in your walk with God?"
                ),
                leaderNotes = "Facilitation Tips: Ensure members contrast 'Religion' (I obey, therefore I am accepted) with the Gospel (I am accepted through Christ, therefore I obey). Emphasize 1 Cor 15:3-4 as the irreducible historical core."
            ),
            LessonEntity(
                id = "lesson_foundational_2",
                guideId = "guide_foundational",
                orderIndex = 2,
                title = "Justification",
                objective = "Grasp the legal declaration where God imputes the righteousness of Christ to believers.",
                keyVerses = listOf("Romans 3:21-26", "Romans 5:1-2", "2 Corinthians 5:21"),
                teachingPoints = listOf(
                    "Justification is a definitive judicial verdict: God declares the ungodly righteous while remaining completely just.",
                    "The Great Exchange: God made Him who knew no sin to be sin for us, that we might become the righteousness of God in Him.",
                    "Peace with God is an objective relational standing, not merely a subjective feeling.",
                    "Works follow justification as fruit; they are never the ground or cause of justification."
                ),
                discussionQuestions = listOf(
                    "What is the difference between being 'made righteous over time' versus being 'declared righteous once and for all'?",
                    "How does Romans 5:1 provide peace when you experience moral failure or spiritual doubt?",
                    "Why was it crucial that God remain 'just' while being 'the justifier' (Romans 3:26)?"
                ),
                leaderNotes = "Leader Note: Watch for the tendency to conflate justification (legal standing) with sanctification (ongoing growth). Remind the group that God's courtroom verdict in Christ can never be appealed or reversed."
            ),
            LessonEntity(
                id = "lesson_foundational_3",
                guideId = "guide_foundational",
                orderIndex = 3,
                title = "Redemption",
                objective = "Discover how Christ purchased our liberation from the slave-market of sin with His own precious blood.",
                keyVerses = listOf("Ephesians 1:7", "Mark 10:45", "1 Peter 1:18-19"),
                teachingPoints = listOf(
                    "Biblical redemption draws on the imagery of paying a ransom price to liberate captives.",
                    "We were enslaved to sin, death, and the power of the enemy with zero ability to ransom ourselves.",
                    "The ransom price was neither silver nor gold, but the spotless blood of Jesus Christ.",
                    "Being redeemed means we now belong wholeheartedly to our Redeemer."
                ),
                discussionQuestions = listOf(
                    "In what ways does modern culture keep people enslaved without them realizing it?",
                    "How does recognizing the price paid for your redemption (1 Peter 1:18-19) alter how you value yourself?",
                    "What area of your life still struggles to live as truly emancipated from sin's power?"
                ),
                leaderNotes = "Suggested Answers: Connect Mark 10:45 to Isaiah 53. Note that 'ransom' in the Greek (lytron) refers to the payment for releasing prisoners of war or slaves."
            ),
            LessonEntity(
                id = "lesson_foundational_4",
                guideId = "guide_foundational",
                orderIndex = 4,
                title = "Regeneration",
                objective = "Explore the supernatural work of the Holy Spirit granting spiritual life to spiritually dead souls.",
                keyVerses = listOf("John 3:3-8", "Titus 3:5", "Ezekiel 36:26-27"),
                teachingPoints = listOf(
                    "Before regeneration, human hearts are spiritually dead, unable to please God or discern spiritual truth.",
                    "The New Birth is sovereignly wrought by the Holy Spirit, like the wind that blows where it wishes.",
                    "God removes our heart of stone and implants a living heart of flesh responsive to His commandments.",
                    "Regeneration produces new desires, new spiritual appetites, and a genuine hatred of sin."
                ),
                discussionQuestions = listOf(
                    "Why did Jesus tell a respectable, moral religious leader like Nicodemus that he 'must be born again'?",
                    "What evidence of a new heart have you noticed in your desires since following Christ?",
                    "How does Titus 3:5 emphasize God's mercy over human merit?"
                ),
                leaderNotes = "Facilitator Tip: Distinguish self-reformation (behavior modification) from spiritual resurrection (the Spirit giving life). Point members to Ezekiel 36."
            ),
            LessonEntity(
                id = "lesson_foundational_5",
                guideId = "guide_foundational",
                orderIndex = 5,
                title = "Adoption",
                objective = "Celebrate our placement as beloved sons and daughters of God with full familial rights and inheritance.",
                keyVerses = listOf("Romans 8:14-17", "Galatians 4:4-7", "1 John 3:1"),
                teachingPoints = listOf(
                    "Justification clears our criminal record; adoption invites us directly to the Father's family table.",
                    "We have received the Spirit of adoption, crying out 'Abba, Father!' in intimate trust.",
                    "As adopted children, we are co-heirs with Christ, sharing in His future glory.",
                    "The Father's loving discipline proves sonship, not rejection or condemnation."
                ),
                discussionQuestions = listOf(
                    "How is the blessing of adoption distinct from the blessing of forgiveness?",
                    "When do you feel most tempted to act like an anxious employee rather than an accepted child of God?",
                    "What does the term 'Abba' signify about the intimacy available in your prayer life?"
                ),
                leaderNotes = "Leader Insight: In Greco-Roman law, an adopted heir was deliberately selected, received all ancestral rights, and prior debts were permanently canceled."
            ),
            LessonEntity(
                id = "lesson_foundational_6",
                guideId = "guide_foundational",
                orderIndex = 6,
                title = "The Nature of God",
                objective = "Gaze upon the infinite holiness, sovereignty, justice, mercy, and triune majesty of God.",
                keyVerses = listOf("Exodus 34:6-7", "Isaiah 6:1-5", "Psalm 103:8-14"),
                teachingPoints = listOf(
                    "God is one essence eternally existing in three distinct persons: Father, Son, and Holy Spirit.",
                    "God is holy: uniquely majestic, morally unblemished, and set apart from all created things.",
                    "He is slow to anger, abounding in steadfast love and faithfulness, maintaining covenant love to thousands.",
                    "His sovereignty ensures that nothing occurs outside His providential wisdom and purpose."
                ),
                discussionQuestions = listOf(
                    "Which of God's attributes comfort you most right now, and which stretch your understanding?",
                    "Why must God's justice and His mercy be kept together without sacrificing either?",
                    "How does Isaiah's encounter with God's holiness (Isaiah 6) transform our casual approach to worship?"
                ),
                leaderNotes = "Theological Note: Be prepared to address how Exodus 34:6-7 balances relentless mercy with righteous judgment, resolved ultimately at the Cross."
            ),
            LessonEntity(
                id = "lesson_foundational_7",
                guideId = "guide_foundational",
                orderIndex = 7,
                title = "Christ",
                objective = "Affirm Jesus Christ as truly God and truly man, our Prophet, Priest, and King.",
                keyVerses = listOf("John 1:1-14", "Colossians 1:15-20", "Hebrews 4:14-16"),
                teachingPoints = listOf(
                    "The Incarnation: the eternal Word became flesh and dwelt among us, full of grace and truth.",
                    "Jesus is the exact imprint of God's nature, holding all creation together by His word of power.",
                    "As our great High Priest, He empathizes with our weaknesses yet was without sin.",
                    "Christ was raised bodily from the dead, ascending to rule at the right hand of the Father."
                ),
                discussionQuestions = listOf(
                    "Why is Jesus' full humanity essential for His work as our mediator?",
                    "Why is Jesus' full deity non-negotiable for our salvation?",
                    "How does knowing Jesus intercedes for you right now (Hebrews 4) encourage your prayer life?"
                ),
                leaderNotes = "Key Discussion: Contrast modern sentimental views of Jesus with the cosmic Lord portrayed in Colossians 1."
            ),
            LessonEntity(
                id = "lesson_foundational_8",
                guideId = "guide_foundational",
                orderIndex = 8,
                title = "The Holy Spirit",
                objective = "Recognize the personal presence, indwelling, conviction, and empowering ministry of the Spirit.",
                keyVerses = listOf("John 14:16-17", "John 16:7-14", "Galatians 5:16-25"),
                teachingPoints = listOf(
                    "The Holy Spirit is not an impersonal force, but the third person of the Trinity with mind, will, and affection.",
                    "The Spirit indwells every believer from conversion as a seal guaranteeing our inheritance.",
                    "He illuminates Scripture, convicts the world of sin, and glorifies Jesus Christ.",
                    "Walking by the Spirit mortifies the flesh and yields love, joy, peace, patience, kindness, goodness, faithfulness, gentleness, and self-control."
                ),
                discussionQuestions = listOf(
                    "What does it practically look like in daily decisions to 'keep in step with the Spirit'?",
                    "How does the Spirit's role of glorifying Jesus protect us from counterfeit spiritual experiences?",
                    "Where do you currently need the Spirit's power against fleshly patterns?"
                ),
                leaderNotes = "Encourage honest sharing around the fruit of the Spirit, guiding the group to see this as character produced by the Spirit, not self-effort."
            ),
            LessonEntity(
                id = "lesson_foundational_9",
                guideId = "guide_foundational",
                orderIndex = 9,
                title = "Scripture",
                objective = "Rely upon the Bible as God's inspired, authoritative, sufficient, and inerrant written revelation.",
                keyVerses = listOf("2 Timothy 3:16-17", "2 Peter 1:20-21", "Psalm 119:105"),
                teachingPoints = listOf(
                    "All Scripture is God-breathed (theopneustos), spoken through human authors carried along by the Holy Spirit.",
                    "Scripture is completely trustworthy and profitable for teaching, rebuking, correcting, and training in righteousness.",
                    "The Bible is sufficient: containing all things necessary for God's glory, human salvation, faith, and obedience.",
                    "God's Word serves as a lamp to our feet and a light to our path in a dark, shifting culture."
                ),
                discussionQuestions = listOf(
                    "What makes trusting the Bible's authority difficult in contemporary culture?",
                    "How does the doctrine of Scripture's sufficiency safeguard us from chasing novel revelations?",
                    "What regular habits help you feast on God's Word rather than merely skimming it?"
                ),
                leaderNotes = "Helpful clarification: 'God-breathed' does not mean mechanical dictation, but divine superintendence over human faculties and language."
            ),
            LessonEntity(
                id = "lesson_foundational_10",
                guideId = "guide_foundational",
                orderIndex = 10,
                title = "Prayer",
                objective = "Cultivate reverent, honest, persistent communication with our heavenly Father.",
                keyVerses = listOf("Matthew 6:9-13", "Philippians 4:6-7", "Romans 8:26-27"),
                teachingPoints = listOf(
                    "Prayer begins with filial reverence: 'Our Father in heaven, hallowed be your name.'",
                    "We align our hearts with God's Kingdom priority before submitting our personal petitions.",
                    "Honest supplication replaces anxiety: presenting requests with thanksgiving welcomes God's surpassing peace.",
                    "When we don't know what to pray, the Spirit Himself intercedes for us with groanings too deep for words."
                ),
                discussionQuestions = listOf(
                    "How does Jesus' model prayer prioritize God's glory before our daily bread?",
                    "What usually hinders your prayer consistency (e.g. distraction, hurry, self-reliance, doubt)?",
                    "How does Romans 8:26 comfort you during seasons of severe grief or exhaustion?"
                ),
                leaderNotes = "Take time at the conclusion of this lesson to pray through Matthew 6 together as a small group."
            ),
            LessonEntity(
                id = "lesson_foundational_11",
                guideId = "guide_foundational",
                orderIndex = 11,
                title = "Sanctification",
                objective = "Engage in the lifelong process of being conformed into the image of Christ by grace.",
                keyVerses = listOf("1 Thessalonians 4:3", "Romans 12:1-2", "Philippians 2:12-13"),
                teachingPoints = listOf(
                    "Positional sanctification occurs at conversion; progressive sanctification unfolds across a lifetime.",
                    "We work out our salvation with fear and trembling because God is at work within us to will and to act.",
                    "Sanctification requires putting off the old self and putting on the new self renewed in knowledge.",
                    "Grace does not breed passivity; grace energizes radical, joyful obedience."
                ),
                discussionQuestions = listOf(
                    "How do we avoid legalism on one side and lawless complacency on the other in sanctification?",
                    "What does Philippians 2:12-13 teach us about the interplay of God's power and our effort?",
                    "What is one worldly mindset you are asking God to renew in your thinking (Romans 12:2)?"
                ),
                leaderNotes = "Highlight: Sanctification is not trying harder in our own strength, but abiding in Christ through the means of grace (Word, prayer, fellowship)."
            ),
            LessonEntity(
                id = "lesson_foundational_12",
                guideId = "guide_foundational",
                orderIndex = 12,
                title = "Repentance",
                objective = "Embrace biblical repentance as an ongoing change of mind and heart leading to renewed life.",
                keyVerses = listOf("2 Corinthians 7:9-10", "Acts 26:20", "Psalm 51:1-12"),
                teachingPoints = listOf(
                    "Repentance is a complete turnaround of mind, heart, and direction regarding sin and God.",
                    "Godly sorrow produces repentance leading to salvation without regret, whereas worldly sorrow produces death.",
                    "Worldly sorrow mourns consequences and caught pride; godly sorrow grieves offending a loving God.",
                    "Repentance is not a one-time initiation rite, but the believer's ongoing daily posture of humility."
                ),
                discussionQuestions = listOf(
                    "How can you discern the difference between godly grief and worldly self-pity?",
                    "Why did Martin Luther state that the entire life of believers should be one of repentance?",
                    "What makes confessing our sin to trusted Christian brothers and sisters so healing yet so intimidating?"
                ),
                leaderNotes = "Read Psalm 51 aloud. Emphasize verse 4: 'Against you, you only, have I sinned' as the diagnostic mark of genuine repentance."
            ),
            LessonEntity(
                id = "lesson_foundational_13",
                guideId = "guide_foundational",
                orderIndex = 13,
                title = "Identity in Christ",
                objective = "Anchor your worth, security, and purpose in your unalterable union with Jesus.",
                keyVerses = listOf("Ephesians 1:3-6", "2 Corinthians 5:17", "Colossians 3:1-4"),
                teachingPoints = listOf(
                    "If anyone is in Christ, he is a new creation; the old has passed away, behold, the new has come.",
                    "We are blessed with every spiritual blessing in the heavenly places in Christ Jesus.",
                    "Our identity is not defined by career, accomplishments, failures, or opinions of others.",
                    "Your life is hidden with Christ in God; when Christ who is your life appears, you also will appear with Him in glory."
                ),
                discussionQuestions = listOf(
                    "What false labels or identities do you most easily internalize when under stress?",
                    "How does being 'chosen, holy, and blameless' before God rewrite your inner narrative?",
                    "In what ways does your union with Christ give you resilience against rejection?"
                ),
                leaderNotes = "Help members contrast 'achieved identity' (earning worth through performance) with 'received identity' (inheriting worth through Christ)."
            ),
            LessonEntity(
                id = "lesson_foundational_14",
                guideId = "guide_foundational",
                orderIndex = 14,
                title = "The Church",
                objective = "Commit wholeheartedly to the gathered people of God as Christ's body, bride, and household.",
                keyVerses = listOf("Acts 2:42-47", "Ephesians 4:11-16", "Hebrews 10:24-25"),
                teachingPoints = listOf(
                    "The church is not a physical building or Sunday event, but the covenant community of the redeemed.",
                    "Early believers devoted themselves to the apostles' teaching, fellowship, breaking of bread, and prayers.",
                    "Every member has spiritual gifts intended for the common good and the building up of the body.",
                    "We do not forsake meeting together, but stir up one another toward love and good deeds."
                ),
                discussionQuestions = listOf(
                    "Why is isolated, consumeristic Christianity toxic to spiritual endurance?",
                    "How does our local church demonstrate the gospel to an watching, fractured world?",
                    "What spiritual gifts or passions do you sense God inviting you to steward for your group and church?"
                ),
                leaderNotes = "Address common church hurt or disillusionment with gentleness, pointing to Christ's unwavering love for His imperfect bride."
            ),
            LessonEntity(
                id = "lesson_foundational_15",
                guideId = "guide_foundational",
                orderIndex = 15,
                title = "Calling & Mission",
                objective = "Live as ambassadors of reconciliation and light in the neighborhood, workplace, and ends of the earth.",
                keyVerses = listOf("Matthew 28:18-20", "2 Corinthians 5:18-20", "Micah 6:8"),
                teachingPoints = listOf(
                    "The Great Commission mandates every believer: make disciples of all nations, baptizing and teaching them.",
                    "We are Christ's ambassadors, God making His appeal through us: 'Be reconciled to God.'",
                    "Our daily vocation is a theater for God's glory, loving our neighbor through honest, excellent work.",
                    "God calls us to act justly, love mercy, and walk humbly with our God in our society."
                ),
                discussionQuestions = listOf(
                    "Who in your sphere of influence (family, work, neighborhood) needs to experience Christ's love through you?",
                    "How can you integrate your secular employment or daily duties with kingdom intentionality?",
                    "What next step of obedience is the Lord speaking to you as we finish this Foundations guide?"
                ),
                leaderNotes = "Commissioning Moment: Conclude this final lesson by laying hands on or praying individually over each member's personal mission field."
            )
        )

        dao.insertLessons(lessons)

        // 6. Group Assignment
        val assignment = GroupGuideAssignmentEntity(
            id = "assign_grace_foundations",
            groupId = "group_grace_fellowship",
            guideId = "guide_foundational",
            schedule = "Weekly - Thursdays at 7:00 PM",
            currentLessonIndex = 1
        )
        dao.insertAssignment(assignment)

        // 7. Seed Initial Notes and Highlights
        val sampleNote1 = NoteEntity(
            id = "note_1",
            userId = "user_me",
            verseRef = "Romans 8:28",
            content = "God does not promise that all things are good in themselves, but that He sovereignly works through all circumstances for the eternal good of those who love Him.",
            visibility = "private"
        )
        val sampleNote2 = NoteEntity(
            id = "note_2",
            userId = "user_me",
            verseRef = "John 1:1",
            content = "The Greek word 'Logos' bridges Hebrew wisdom (the creating Word of God) and Greek philosophy (the governing principle of the cosmos). Christ is the divine personal embodiment of both.",
            visibility = "group"
        )
        dao.insertNote(sampleNote1)
        dao.insertNote(sampleNote2)

        val sampleHighlight1 = HighlightEntity(
            id = "hl_1",
            userId = "user_me",
            verseRef = "Romans 8:28",
            colorTag = "Promises"
        )
        val sampleHighlight2 = HighlightEntity(
            id = "hl_2",
            userId = "user_me",
            verseRef = "Romans 8:38",
            colorTag = "Identity in Christ"
        )
        dao.insertHighlight(sampleHighlight1)
        dao.insertHighlight(sampleHighlight2)

        // 8. Seed Sample Discussion Posts for Lesson 1
        val posts = listOf(
            DiscussionPostEntity(
                id = "post_1",
                lessonId = "lesson_foundational_1",
                groupId = "group_grace_fellowship",
                userId = "user_sarah",
                userName = "Sarah Jenkins",
                userRole = "Member",
                content = "Reflecting on question 1: For years I treated Christianity like a performance treadmill. Hearing that the gospel is finished news took an immense weight off my chest today.",
                questionNumber = 1,
                createdAt = System.currentTimeMillis() - 86400000L
            ),
            DiscussionPostEntity(
                id = "post_2",
                lessonId = "lesson_foundational_1",
                groupId = "group_grace_fellowship",
                userId = "user_david",
                userName = "David Chen",
                userRole = "Member",
                content = "Regarding Q3: In my workplace culture, everything is based on earned bonuses and status. Ephesians 2:8-9 reminds me that with God, boasting is permanently silenced.",
                questionNumber = 3,
                createdAt = System.currentTimeMillis() - 43200000L
            )
        )
        posts.forEach { dao.insertDiscussionPost(it) }

        // 9. Initial Reading Progress
        val progress = ReadingProgressEntity(
            id = "progress_default",
            userId = "user_me",
            planId = "annual_plan",
            currentBook = "Romans",
            currentChapter = 8,
            currentVerse = 28,
            streakDays = 12,
            lastReadTimestamp = System.currentTimeMillis()
        )
        dao.updateReadingProgress(progress)

        // 10. Sample completion
        dao.insertCompletion(
            LessonCompletionEntity(
                id = "lesson_foundational_1-user_sarah",
                lessonId = "lesson_foundational_1",
                userId = "user_sarah",
                userName = "Sarah Jenkins",
                groupId = "group_grace_fellowship"
            )
        )
    }
}
